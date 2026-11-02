package atm;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ATMService - Business Logic and Data Access Layer.
 *
 * All database operations strictly adhere to:
 * 1. PreparedStatement usage to prevent SQL Injection attacks.
 * 2. BigDecimal for precise financial calculations (avoids IEEE 754 floating-point errors).
 * 3. ACID compliant database transactions: Both balance update and transaction audit log
 *    are committed together or completely rolled back on error.
 */
public class ATMService {

    // ========================================================================
    // DATA TRANSFER OBJECTS (DTOs) / MODELS
    // ========================================================================

    /**
     * Represents a customer bank account record in the system.
     */
    public static class Account {
        private final String accountNumber;
        private final String holderName;
        private final String pin;
        private final BigDecimal balance;

        public Account(String accountNumber, String holderName, String pin, BigDecimal balance) {
            this.accountNumber = accountNumber;
            this.holderName = holderName;
            this.pin = pin;
            this.balance = balance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getHolderName() { return holderName; }
        public String getPin() { return pin; }
        public BigDecimal getBalance() { return balance; }
    }

    /**
     * Represents a transaction audit log item (Deposit or Withdrawal).
     */
    public static class TransactionRecord {
        private final int transactionId;
        private final String accountNumber;
        private final String transactionType;
        private final BigDecimal amount;
        private final Timestamp transactionDate;

        public TransactionRecord(int transactionId, String accountNumber, String transactionType,
                                 BigDecimal amount, Timestamp transactionDate) {
            this.transactionId = transactionId;
            this.accountNumber = accountNumber;
            this.transactionType = transactionType;
            this.amount = amount;
            this.transactionDate = transactionDate;
        }

        public int getTransactionId() { return transactionId; }
        public String getAccountNumber() { return accountNumber; }
        public String getTransactionType() { return transactionType; }
        public BigDecimal getAmount() { return amount; }
        public Timestamp getTransactionDate() { return transactionDate; }
    }

    // ========================================================================
    // CUSTOM EXCEPTIONS FOR USER-FRIENDLY GUI FEEDBACK
    // ========================================================================

    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) { super(message); }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) { super(message); }
    }

    public static class InsufficientBalanceException extends Exception {
        public InsufficientBalanceException(String message) { super(message); }
    }

    // ========================================================================
    // INPUT VALIDATION HELPERS
    // ========================================================================

    /**
     * Validates that monetary amounts are strictly positive and have at most 2 decimal places.
     *
     * @param amount the input BigDecimal
     * @throws ValidationException if the amount is null, zero, negative, or has >2 decimals
     */
    public static void validateAmount(BigDecimal amount) throws ValidationException {
        if (amount == null) {
            throw new ValidationException("Please enter a valid monetary amount.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero.");
        }
        // Check decimal places (scale). e.g., 50.25 is valid (2 decimals), 50.255 is invalid (3 decimals)
        if (amount.scale() > 2 && amount.stripTrailingZeros().scale() > 2) {
            throw new ValidationException("Amount cannot have more than 2 decimal places (paise).");
        }
    }

    /**
     * Validates account number format.
     */
    public static void validateAccountNumber(String accountNumber) throws ValidationException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new ValidationException("Account Number cannot be blank.");
        }
        if (!accountNumber.trim().matches("^[0-9A-Za-z_-]{4,20}$")) {
            throw new ValidationException("Invalid Account Number format. Must be between 4 and 20 alphanumeric characters.");
        }
    }

    /**
     * Validates 4-digit numeric PIN format.
     */
    public static void validatePin(String pin) throws ValidationException {
        if (pin == null || pin.trim().isEmpty()) {
            throw new ValidationException("PIN cannot be blank.");
        }
        if (!pin.matches("^\\d{4}$")) {
            throw new ValidationException("PIN must be exactly 4 numeric digits (0-9).");
        }
    }

    // ========================================================================
    // CORE ATM OPERATIONS
    // ========================================================================

    /**
     * Authenticates an account holder by account number and 4-digit PIN.
     *
     * @param accountNumber the customer account number
     * @param pin the 4-digit PIN
     * @return the authenticated Account object
     * @throws AuthenticationException if account not found or PIN is incorrect
     * @throws ValidationException if input format is invalid
     * @throws SQLException if a database error occurs
     */
    public Account authenticate(String accountNumber, String pin)
            throws AuthenticationException, ValidationException, SQLException {
        validateAccountNumber(accountNumber);
        validatePin(pin);

        String sql = "SELECT account_number, holder_name, pin, balance FROM accounts WHERE account_number = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new AuthenticationException("Account '" + accountNumber + "' not found. Please check your account number.");
                }

                String storedPin = rs.getString("pin");
                if (!storedPin.equals(pin)) {
                    throw new AuthenticationException("Incorrect PIN entered. Please try again.");
                }

                String holderName = rs.getString("holder_name");
                BigDecimal balance = rs.getBigDecimal("balance");

                return new Account(accountNumber.trim(), holderName, storedPin, balance);
            }
        }
    }

    /**
     * Fetches the latest balance for an account directly from the database.
     *
     * @param accountNumber the account number to check
     * @return current BigDecimal balance
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getBalance(String accountNumber) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("balance");
                } else {
                    throw new SQLException("Account '" + accountNumber + "' does not exist.");
                }
            }
        }
    }

    /**
     * Performs an atomic cash deposit.
     *
     * Transaction Guarantee:
     * Balance update and transaction history record are committed together.
     * If either fails, the entire transaction is rolled back.
     *
     * @param accountNumber target account number
     * @param amount monetary amount to deposit
     * @return the new updated balance
     * @throws ValidationException if amount is invalid
     * @throws SQLException if a database error occurs during the transaction
     */
    public BigDecimal deposit(String accountNumber, BigDecimal amount)
            throws ValidationException, SQLException {
        validateAmount(amount);

        String updateBalanceSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        String insertTxSql = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, 'DEPOSIT', ?)";
        String selectBalanceSql = "SELECT balance FROM accounts WHERE account_number = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            // Start explicit transaction: disable auto-commit
            conn.setAutoCommit(false);

            // Step 1: Update the account balance
            try (PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSql)) {
                updateStmt.setBigDecimal(1, amount);
                updateStmt.setString(2, accountNumber);
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Account not found for deposit: " + accountNumber);
                }
            }

            // Step 2: Record audit log in transactions table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertTxSql)) {
                insertStmt.setString(1, accountNumber);
                insertStmt.setBigDecimal(2, amount);
                insertStmt.executeUpdate();
            }

            // Step 3: Fetch updated balance within the same transaction
            BigDecimal newBalance;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectBalanceSql)) {
                selectStmt.setString(1, accountNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        newBalance = rs.getBigDecimal("balance");
                    } else {
                        throw new SQLException("Failed to retrieve updated balance.");
                    }
                }
            }

            // Step 4: Commit both operations atomically
            conn.commit();
            return newBalance;

        } catch (SQLException ex) {
            // Roll back all changes if any query failed
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[ERROR] Failed to rollback transaction: " + rollbackEx.getMessage());
                }
            }
            throw ex;
        } finally {
            // Restore auto-commit and close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    /**
     * Performs an atomic cash withdrawal with insufficient balance check.
     *
     * Transaction Guarantee:
     * Balance check, balance deduction, and audit log insert happen in a single
     * ACID transaction with pessimistic row lock (FOR UPDATE).
     *
     * @param accountNumber target account number
     * @param amount monetary amount to withdraw
     * @return the new updated balance
     * @throws ValidationException if amount is invalid
     * @throws InsufficientBalanceException if requested amount exceeds current balance
     * @throws SQLException if a database error occurs
     */
    public BigDecimal withdraw(String accountNumber, BigDecimal amount)
            throws ValidationException, InsufficientBalanceException, SQLException {
        validateAmount(amount);

        String lockBalanceSql = "SELECT balance FROM accounts WHERE account_number = ? FOR UPDATE";
        String updateBalanceSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        String insertTxSql = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, 'WITHDRAWAL', ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            // Start explicit transaction
            conn.setAutoCommit(false);

            BigDecimal currentBalance;

            // Step 1: Read and lock current balance for this account
            try (PreparedStatement lockStmt = conn.prepareStatement(lockBalanceSql)) {
                lockStmt.setString(1, accountNumber);
                try (ResultSet rs = lockStmt.executeQuery()) {
                    if (rs.next()) {
                        currentBalance = rs.getBigDecimal("balance");
                    } else {
                        throw new SQLException("Account not found: " + accountNumber);
                    }
                }
            }

            // Step 2: Validate sufficient funds
            if (currentBalance.compareTo(amount) < 0) {
                throw new InsufficientBalanceException(
                    String.format("Insufficient balance! Available: ₹%.2f, Requested: ₹%.2f",
                            currentBalance, amount)
                );
            }

            // Step 3: Deduct amount from balance
            try (PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSql)) {
                updateStmt.setBigDecimal(1, amount);
                updateStmt.setString(2, accountNumber);
                updateStmt.executeUpdate();
            }

            // Step 4: Record audit log in transactions table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertTxSql)) {
                insertStmt.setString(1, accountNumber);
                insertStmt.setBigDecimal(2, amount);
                insertStmt.executeUpdate();
            }

            // Step 5: Commit changes atomically
            conn.commit();
            return currentBalance.subtract(amount);

        } catch (SQLException | InsufficientBalanceException ex) {
            // Roll back if error or insufficient balance
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[ERROR] Failed to rollback transaction: " + rollbackEx.getMessage());
                }
            }
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves the complete transaction audit log for an account, sorted newest first.
     *
     * @param accountNumber the target account number
     * @return List of TransactionRecord objects
     * @throws SQLException if a database error occurs
     */
    public List<TransactionRecord> getTransactionHistory(String accountNumber) throws SQLException {
        String sql = "SELECT transaction_id, account_number, transaction_type, amount, transaction_date " +
                     "FROM transactions WHERE account_number = ? " +
                     "ORDER BY transaction_date DESC, transaction_id DESC";

        List<TransactionRecord> history = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int txId = rs.getInt("transaction_id");
                    String accNum = rs.getString("account_number");
                    String type = rs.getString("transaction_type");
                    BigDecimal amt = rs.getBigDecimal("amount");
                    Timestamp txDate = rs.getTimestamp("transaction_date");

                    history.add(new TransactionRecord(txId, accNum, type, amt, txDate));
                }
            }
        }

        return history;
    }
}
