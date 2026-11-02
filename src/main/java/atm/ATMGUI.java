package atm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * ATMGUI - Graphical User Interface for the Classroom ATM Simulation.
 *
 * Built with Java Swing following best practices:
 * 1. CardLayout transitions between the Login Screen and Account Dashboard.
 * 2. Asynchronous execution: All database calls are executed off the Event Dispatch Thread (EDT)
 *    using SwingWorker, ensuring the user interface remains completely responsive and never freezes.
 * 3. Monetary values are strictly displayed with the Indian Rupee symbol (₹) and two decimal places.
 * 4. Comprehensive client-side and server-side validation with student-friendly dialogs.
 */
public class ATMGUI extends JFrame {

    // CardLayout identifiers
    private static final String CARD_LOGIN = "CARD_LOGIN";
    private static final String CARD_DASHBOARD = "CARD_DASHBOARD";

    // Currency and date formatters
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("₹#,##0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Colors for modern clean theme
    private static final Color PRIMARY_COLOR = new Color(24, 76, 120);     // Deep Bank Blue
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185); // Slate Blue
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);    // Emerald Green
    private static final Color DANGER_COLOR = new Color(192, 57, 43);     // Crimson Red
    private static final Color BG_COLOR = new Color(245, 247, 250);       // Soft Gray Background

    // Services & State
    private final ATMService atmService;
    private ATMService.Account currentAccount;

    // GUI Layout components
    private final CardLayout cardLayout;
    private final JPanel mainContainer;

    // Login screen components
    private JTextField txtAccountNumber;
    private JPasswordField txtPin;
    private JButton btnLogin;
    private JLabel lblLoginStatus;

    // Dashboard screen components
    private JLabel lblWelcomeHolder;
    private JLabel lblAccountNumber;
    private JLabel lblCurrentBalance;
    private JButton btnCheckBalance;
    private JButton btnDeposit;
    private JButton btnWithdraw;
    private JButton btnHistory;
    private JButton btnLogout;

    /**
     * Constructs and initializes the ATM Simulation Window.
     */
    public ATMGUI() {
        super("Classroom ATM Simulation - Practical Project");
        this.atmService = new ATMService();

        // Basic frame configuration
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 560);
        setMinimumSize(new Dimension(680, 500));
        setLocationRelativeTo(null); // Center on screen

        // Setup CardLayout container
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(BG_COLOR);

        // Build screens
        mainContainer.add(createLoginPanel(), CARD_LOGIN);
        mainContainer.add(createDashboardPanel(), CARD_DASHBOARD);

        add(mainContainer);

        // Start at Login screen
        cardLayout.show(mainContainer, CARD_LOGIN);
    }

    // ========================================================================
    // SCREEN 1: LOGIN PANEL
    // ========================================================================

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header Banner
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel titleLabel = new JLabel("CLASSROOM ATM SIMULATION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subTitleLabel = new JLabel("Academic Practical Simulation (Fake Money • Demo Account)", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(new Color(220, 230, 242));

        headerPanel.add(titleLabel);
        headerPanel.add(subTitleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Center Login Card
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BG_COLOR);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 224, 233), 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Card Title
        JLabel lblCardTitle = new JLabel("Account Login", SwingConstants.CENTER);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCardTitle.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        cardPanel.add(lblCardTitle, gbc);

        // Account Number field
        JLabel lblAcc = new JLabel("Account Number:");
        lblAcc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        cardPanel.add(lblAcc, gbc);

        txtAccountNumber = new JTextField(15);
        txtAccountNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        cardPanel.add(txtAccountNumber, gbc);

        // PIN field
        JLabel lblPin = new JLabel("4-Digit PIN:");
        lblPin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2;
        cardPanel.add(lblPin, gbc);

        txtPin = new JPasswordField(15);
        txtPin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        cardPanel.add(txtPin, gbc);

        // Login Status / Error message label
        lblLoginStatus = new JLabel(" ", SwingConstants.CENTER);
        lblLoginStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLoginStatus.setForeground(DANGER_COLOR);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        cardPanel.add(lblLoginStatus, gbc);

        // Action Buttons: Login & Auto-fill Demo
        btnLogin = new JButton("Enter ATM / Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(this::handleLogin);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        cardPanel.add(btnLogin, gbc);

        JButton btnDemoFill = new JButton("Fill Demo Account (1001001 / 1234)");
        btnDemoFill.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDemoFill.setBackground(new Color(240, 244, 248));
        btnDemoFill.setForeground(new Color(60, 80, 100));
        btnDemoFill.setFocusPainted(false);
        btnDemoFill.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDemoFill.addActionListener(e -> {
            txtAccountNumber.setText("1001001");
            txtPin.setText("1234");
            lblLoginStatus.setText("Demo credentials filled. Click Login.");
            lblLoginStatus.setForeground(new Color(41, 128, 185));
        });
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        cardPanel.add(btnDemoFill, gbc);

        centerWrapper.add(cardPanel);
        panel.add(centerWrapper, BorderLayout.CENTER);

        // Support pressing Enter key in text fields to trigger login
        txtAccountNumber.addActionListener(this::handleLogin);
        txtPin.addActionListener(this::handleLogin);

        // Footer Note
        JLabel footerNote = new JLabel("Note: Demo data persists in MySQL. Plaintext PIN is strictly for academic viva demonstration.", SwingConstants.CENTER);
        footerNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footerNote.setForeground(Color.GRAY);
        footerNote.setBorder(new EmptyBorder(10, 10, 15, 10));
        panel.add(footerNote, BorderLayout.SOUTH);

        return panel;
    }

    // ========================================================================
    // SCREEN 2: DASHBOARD PANEL
    // ========================================================================

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header Panel: Account Details & Bank Title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(18, 24, 18, 24));

        JPanel leftHeader = new JPanel(new GridLayout(2, 1, 0, 2));
        leftHeader.setOpaque(false);

        lblWelcomeHolder = new JLabel("Welcome, Student!");
        lblWelcomeHolder.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcomeHolder.setForeground(Color.WHITE);

        lblAccountNumber = new JLabel("Account: #1001001");
        lblAccountNumber.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAccountNumber.setForeground(new Color(220, 230, 242));

        leftHeader.add(lblWelcomeHolder);
        leftHeader.add(lblAccountNumber);

        // Logout Button in header
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(this::handleLogout);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Center Content: Balance Display Card & ATM Action Buttons
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        // Balance Card Display
        JPanel balanceCard = new JPanel(new BorderLayout(5, 5));
        balanceCard.setBackground(Color.WHITE);
        balanceCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 224, 233), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblBalTitle = new JLabel("CURRENT ACCOUNT BALANCE", SwingConstants.CENTER);
        lblBalTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBalTitle.setForeground(new Color(120, 140, 160));

        lblCurrentBalance = new JLabel("₹0.00", SwingConstants.CENTER);
        lblCurrentBalance.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblCurrentBalance.setForeground(SUCCESS_COLOR);

        JLabel lblFakeNotice = new JLabel("Virtual Demonstration Currency (Non-transferable)", SwingConstants.CENTER);
        lblFakeNotice.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFakeNotice.setForeground(Color.GRAY);

        balanceCard.add(lblBalTitle, BorderLayout.NORTH);
        balanceCard.add(lblCurrentBalance, BorderLayout.CENTER);
        balanceCard.add(lblFakeNotice, BorderLayout.SOUTH);
        centerPanel.add(balanceCard, BorderLayout.NORTH);

        // Action Buttons Grid (2 x 2)
        JPanel actionsGrid = new JPanel(new GridLayout(2, 2, 18, 18));
        actionsGrid.setOpaque(false);

        btnCheckBalance = createMenuButton("1. Check Balance", "Refresh and display your current account balance", SECONDARY_COLOR);
        btnCheckBalance.addActionListener(e -> refreshBalance(true));

        btnDeposit = createMenuButton("2. Deposit Cash", "Add fake money to your demo account (ACID transaction)", SUCCESS_COLOR);
        btnDeposit.addActionListener(this::handleDeposit);

        btnWithdraw = createMenuButton("3. Withdraw Cash", "Withdraw funds with automatic balance validation", new Color(230, 126, 34));
        btnWithdraw.addActionListener(this::handleWithdrawal);

        btnHistory = createMenuButton("4. Mini Statement", "View transaction history table (newest first)", PRIMARY_COLOR);
        btnHistory.addActionListener(this::handleTransactionHistory);

        actionsGrid.add(btnCheckBalance);
        actionsGrid.add(btnDeposit);
        actionsGrid.add(btnWithdraw);
        actionsGrid.add(btnHistory);

        centerPanel.add(actionsGrid, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Helper to create uniformly styled menu action buttons.
     */
    private JButton createMenuButton(String title, String subtitle, Color accentColor) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(4, 4));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 224, 233), 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(accentColor);

        JLabel lblSub = new JLabel("<html><p style='width:200px'>" + subtitle + "</p></html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(110, 125, 140));

        button.add(lblTitle, BorderLayout.NORTH);
        button.add(lblSub, BorderLayout.CENTER);

        return button;
    }

    // ========================================================================
    // EVENT HANDLERS & ASYNCHRONOUS SWINGWORKERS
    // ========================================================================

    /**
     * Handles user login off the EDT using SwingWorker.
     */
    private void handleLogin(ActionEvent e) {
        String accNum = txtAccountNumber.getText().trim();
        String pin = new String(txtPin.getPassword()).trim();

        lblLoginStatus.setText(" ");

        // Client-side quick validation before invoking database
        try {
            ATMService.validateAccountNumber(accNum);
            ATMService.validatePin(pin);
        } catch (ATMService.ValidationException ex) {
            lblLoginStatus.setText(ex.getMessage());
            lblLoginStatus.setForeground(DANGER_COLOR);
            return;
        }

        // Disable login button and set waiting cursor while querying DB
        btnLogin.setEnabled(false);
        btnLogin.setText("Authenticating...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // SwingWorker executes DB call on background worker thread
        SwingWorker<ATMService.Account, Void> loginWorker = new SwingWorker<>() {
            @Override
            protected ATMService.Account doInBackground() throws Exception {
                return atmService.authenticate(accNum, pin);
            }

            @Override
            protected void done() {
                try {
                    currentAccount = get(); // Retrieves authenticated account
                    // Update Dashboard UI
                    lblWelcomeHolder.setText("Welcome, " + currentAccount.getHolderName() + "!");
                    lblAccountNumber.setText("Account: #" + currentAccount.getAccountNumber());
                    lblCurrentBalance.setText(CURRENCY_FORMAT.format(currentAccount.getBalance()));

                    // Clear login inputs
                    txtAccountNumber.setText("");
                    txtPin.setText("");
                    lblLoginStatus.setText(" ");

                    // Transition to Dashboard screen
                    cardLayout.show(mainContainer, CARD_DASHBOARD);

                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    String message = cause.getMessage();

                    if (cause instanceof java.sql.SQLException) {
                        message = "Database Error: " + message + "\n\nTip: Ensure MySQL is running and password is set in DBConnection.java.";
                    }
                    lblLoginStatus.setText(cause.getMessage());
                    lblLoginStatus.setForeground(DANGER_COLOR);
                    JOptionPane.showMessageDialog(ATMGUI.this,
                            message,
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Enter ATM / Login");
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        loginWorker.execute();
    }

    /**
     * Refreshes the balance from MySQL database using SwingWorker.
     *
     * @param showDialog true if a confirmation popup should be displayed
     */
    private void refreshBalance(boolean showDialog) {
        if (currentAccount == null) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setDashboardButtonsEnabled(false);

        SwingWorker<BigDecimal, Void> balanceWorker = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                return atmService.getBalance(currentAccount.getAccountNumber());
            }

            @Override
            protected void done() {
                try {
                    BigDecimal balance = get();
                    lblCurrentBalance.setText(CURRENCY_FORMAT.format(balance));

                    if (showDialog) {
                        JOptionPane.showMessageDialog(ATMGUI.this,
                                "Account Holder: " + currentAccount.getHolderName() +
                                "\nAccount Number: " + currentAccount.getAccountNumber() +
                                "\n\nAvailable Balance: " + CURRENCY_FORMAT.format(balance),
                                "Balance Inquiry",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    handleDbException(ex, "Failed to fetch balance");
                } finally {
                    setDashboardButtonsEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        balanceWorker.execute();
    }

    /**
     * Handles Cash Deposit transaction.
     */
    private void handleDeposit(ActionEvent e) {
        if (currentAccount == null) return;

        String input = JOptionPane.showInputDialog(this,
                "Enter cash amount to deposit (₹):",
                "Deposit Cash",
                JOptionPane.PLAIN_MESSAGE);

        if (input == null) return; // User pressed Cancel
        input = input.trim();
        if (input.isEmpty()) return;

        BigDecimal amount;
        try {
            amount = new BigDecimal(input);
            ATMService.validateAmount(amount);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input! Please enter a valid numerical amount (e.g. 500 or 1500.50).",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } catch (ATMService.ValidationException ve) {
            JOptionPane.showMessageDialog(this,
                    ve.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Run atomic deposit transaction off EDT
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setDashboardButtonsEnabled(false);

        final BigDecimal depositAmt = amount;
        SwingWorker<BigDecimal, Void> depositWorker = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                return atmService.deposit(currentAccount.getAccountNumber(), depositAmt);
            }

            @Override
            protected void done() {
                try {
                    BigDecimal newBalance = get();
                    lblCurrentBalance.setText(CURRENCY_FORMAT.format(newBalance));

                    JOptionPane.showMessageDialog(ATMGUI.this,
                            "Deposit Successful!\n\n" +
                            "Amount Deposited: " + CURRENCY_FORMAT.format(depositAmt) + "\n" +
                            "Updated Balance: " + CURRENCY_FORMAT.format(newBalance) + "\n\n" +
                            "(Recorded atomically in transactions table)",
                            "Transaction Complete",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    handleDbException(ex, "Deposit Failed");
                } finally {
                    setDashboardButtonsEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        depositWorker.execute();
    }

    /**
     * Handles Cash Withdrawal transaction with insufficient funds prevention.
     */
    private void handleWithdrawal(ActionEvent e) {
        if (currentAccount == null) return;

        String input = JOptionPane.showInputDialog(this,
                "Enter amount to withdraw (₹):",
                "Withdraw Cash",
                JOptionPane.PLAIN_MESSAGE);

        if (input == null) return; // Cancelled
        input = input.trim();
        if (input.isEmpty()) return;

        BigDecimal amount;
        try {
            amount = new BigDecimal(input);
            ATMService.validateAmount(amount);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input! Please enter a valid numerical amount (e.g., 500 or 1000.00).",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } catch (ATMService.ValidationException ve) {
            JOptionPane.showMessageDialog(this,
                    ve.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Run atomic withdrawal transaction off EDT
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setDashboardButtonsEnabled(false);

        final BigDecimal withdrawAmt = amount;
        SwingWorker<BigDecimal, Void> withdrawWorker = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                return atmService.withdraw(currentAccount.getAccountNumber(), withdrawAmt);
            }

            @Override
            protected void done() {
                try {
                    BigDecimal newBalance = get();
                    lblCurrentBalance.setText(CURRENCY_FORMAT.format(newBalance));

                    JOptionPane.showMessageDialog(ATMGUI.this,
                            "Withdrawal Successful!\n\n" +
                            "Please collect your cash: " + CURRENCY_FORMAT.format(withdrawAmt) + "\n" +
                            "Remaining Balance: " + CURRENCY_FORMAT.format(newBalance),
                            "Cash Dispensed",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    if (cause instanceof ATMService.InsufficientBalanceException) {
                        JOptionPane.showMessageDialog(ATMGUI.this,
                                cause.getMessage(),
                                "Transaction Declined",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        handleDbException(ex, "Withdrawal Failed");
                    }
                } finally {
                    setDashboardButtonsEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        withdrawWorker.execute();
    }

    /**
     * Displays transaction history in a JTable modal (newest first).
     */
    private void handleTransactionHistory(ActionEvent e) {
        if (currentAccount == null) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setDashboardButtonsEnabled(false);

        SwingWorker<List<ATMService.TransactionRecord>, Void> historyWorker = new SwingWorker<>() {
            @Override
            protected List<ATMService.TransactionRecord> doInBackground() throws Exception {
                return atmService.getTransactionHistory(currentAccount.getAccountNumber());
            }

            @Override
            protected void done() {
                try {
                    List<ATMService.TransactionRecord> history = get();
                    showTransactionHistoryDialog(history);
                } catch (Exception ex) {
                    handleDbException(ex, "Failed to fetch transaction history");
                } finally {
                    setDashboardButtonsEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        historyWorker.execute();
    }

    /**
     * Builds and shows a custom styled modal dialog containing the JTable of transactions.
     */
    private void showTransactionHistoryDialog(List<ATMService.TransactionRecord> history) {
        JDialog dialog = new JDialog(this, "Mini Statement - Transaction History", true);
        dialog.setSize(640, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Column definitions
        String[] columnNames = {"Tx ID", "Date & Time", "Type", "Amount (₹)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        for (ATMService.TransactionRecord tx : history) {
            String formattedDate = tx.getTransactionDate() != null
                    ? DATE_FORMAT.format(tx.getTransactionDate()) : "N/A";
            String formattedAmount = CURRENCY_FORMAT.format(tx.getAmount());
            model.addRow(new Object[]{
                    "#" + tx.getTransactionId(),
                    formattedDate,
                    tx.getTransactionType(),
                    formattedAmount
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(235, 240, 245));

        // Center align columns except Amount (right align)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Header info in dialog
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBorder(new EmptyBorder(12, 16, 5, 16));
        header.setBackground(Color.WHITE);

        JLabel lblAcc = new JLabel("Account Number: " + currentAccount.getAccountNumber() + " (" + currentAccount.getHolderName() + ")");
        lblAcc.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblCount = new JLabel("Total Transactions: " + history.size() + " (Sorted: Newest First)");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCount.setForeground(Color.GRAY);

        header.add(lblAcc);
        header.add(lblCount);

        // Close button at bottom
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Close Statement");
        btnClose.addActionListener(ev -> dialog.dispose());
        footer.add(btnClose);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Handles Logout by resetting session data and returning to Login Screen.
     */
    private void handleLogout(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out and end your ATM session?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            currentAccount = null;
            txtAccountNumber.setText("");
            txtPin.setText("");
            lblLoginStatus.setText("You have logged out safely.");
            lblLoginStatus.setForeground(SUCCESS_COLOR);
            cardLayout.show(mainContainer, CARD_LOGIN);
        }
    }

    /**
     * Enables or disables dashboard buttons during background tasks.
     */
    private void setDashboardButtonsEnabled(boolean enabled) {
        btnCheckBalance.setEnabled(enabled);
        btnDeposit.setEnabled(enabled);
        btnWithdraw.setEnabled(enabled);
        btnHistory.setEnabled(enabled);
        btnLogout.setEnabled(enabled);
    }

    /**
     * Standardized database exception dialog handler.
     */
    private void handleDbException(Exception ex, String title) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        cause.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error: " + cause.getMessage() + "\n\nPlease check your MySQL connection and try again.",
                title,
                JOptionPane.ERROR_MESSAGE);
    }
}
