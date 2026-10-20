CREATE DATABASE IF NOT EXISTS banking_system;
USE banking_system;

CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    holder_name VARCHAR(120) NOT NULL,
    balance DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_nonnegative_balance CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS account_transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    transaction_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_positive_transaction_amount CHECK (amount > 0),
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES bank_accounts(account_id)
);
