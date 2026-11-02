-- ============================================================================
-- Classroom ATM Simulation Database Schema
-- Database Name: atm_db
-- Target: MySQL 8.0+
-- 
-- IMPORTANT EDUCATIONAL NOTE ON SECURITY:
-- In this academic simulation, account PINs are stored in plain text ('1234')
-- strictly for demonstration, testing, and ease of grading.
-- In a real-world enterprise banking system, plain-text PINs or passwords
-- MUST NEVER be stored. Real systems require cryptographic salted one-way
-- hashing algorithms (e.g., Argon2id, bcrypt, or PBKDF2), hardware security
-- modules (HSMs), and encrypted TLS/SSL communication channels.
-- ============================================================================

-- Step 1: Create the database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS atm_db;
USE atm_db;

-- Step 2: Create the 'accounts' table
-- Stores bank account details, cardholder name, PIN, and current balance
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    pin VARCHAR(100) NOT NULL,
    balance DECIMAL(12, 2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Step 3: Create the 'transactions' table
-- Stores audit log for all ATM deposits and withdrawals with timestamps
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,  -- 'DEPOSIT' or 'WITHDRAWAL'
    amount DECIMAL(12, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_number 
        FOREIGN KEY (account_number) 
        REFERENCES accounts(account_number) 
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Step 4: Seed initial Demo Student account
-- Account Number: 1001001
-- Account Holder: Demo Student
-- PIN: 1234
-- Initial Balance: ₹5,000.00
INSERT INTO accounts (account_number, holder_name, pin, balance)
VALUES ('1001001', 'Demo Student', '1234', 5000.00)
ON DUPLICATE KEY UPDATE 
    holder_name = VALUES(holder_name),
    pin = VALUES(pin),
    balance = VALUES(balance);

-- Step 5: Seed initial opening balance transaction record for history demonstration
INSERT INTO transactions (account_number, transaction_type, amount, transaction_date)
VALUES ('1001001', 'DEPOSIT', 5000.00, CURRENT_TIMESTAMP);
