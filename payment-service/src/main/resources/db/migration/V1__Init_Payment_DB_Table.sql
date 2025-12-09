SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES (존재한다면 먼저 삭제)
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS honey_wallet;
DROP TABLE IF EXISTS honey_history;

-- Create Table

-- 1. Payment 테이블
CREATE TABLE payment
(
    payment_id BIGINT NOT NULL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    payment_key VARCHAR(200) NOT NULL,
    order_id VARCHAR(64) NOT NULL,
    amount INT NOT NULL,
    status ENUM('PENDING', 'PAID', 'CANCELED') NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT UQ_payment_key UNIQUE (payment_key),
    CONSTRAINT UQ_order_id UNIQUE (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. Honey Wallet 테이블
CREATE TABLE honey_wallet (
    honey_wallet_id BIGINT NOT NULL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    balance BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT UQ_member_id UNIQUE (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3. Honey History 테이블
CREATE TABLE honey_history
(
    honey_history_id BIGINT NOT NULL PRIMARY KEY,
    honey_wallet_id BIGINT NOT NULL,
    target_member_id BIGINT NOT NULL,
    amount INT NOT NULL,
    balance INT NOT NULL,
    type ENUM('CHARGE', 'PAYMENT', 'EARNING', 'WITHDRAW') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_history_TO_wallet
       FOREIGN KEY (honey_wallet_id)
           REFERENCES Honey_wallet (honey_wallet_id)
           ON DELETE RESTRICT
           ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;