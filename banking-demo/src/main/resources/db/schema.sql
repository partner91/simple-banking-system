CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(20)
);

CREATE TABLE Account (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255),
    account_type VARCHAR(255),
    balance DECIMAL(19, 2),
    past_month_turnover DECIMAL(19, 2),
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

CREATE TABLE Transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_account_id INT,
    receiver_account_id INT,
    amount DECIMAL(19, 2),
    currency_id VARCHAR(3),
    message VARCHAR(255),
    time_stamp TIMESTAMP,
    FOREIGN KEY (sender_account_id) REFERENCES Account(account_id),
    FOREIGN KEY (receiver_account_id) REFERENCES Account(account_id)
);


