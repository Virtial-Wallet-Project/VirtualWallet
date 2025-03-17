CREATE TABLE users
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(20)    NOT NULL UNIQUE,
    password     VARCHAR(255)   NOT NULL,
    email        VARCHAR(255)   NOT NULL UNIQUE,
    phone_number VARCHAR(10)    NOT NULL UNIQUE,
    balance      DECIMAL(10, 2) NOT NULL DEFAULT 0.0,
    is_admin     BOOLEAN                 DEFAULT FALSE,
    is_blocked   BOOLEAN                 DEFAULT FALSE,
    verification_token VARCHAR(255) UNIQUE,
    account_verified   BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE credit_cards
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT         NOT NULL,
    card_number     VARCHAR(16) NOT NULL UNIQUE,
    expiration_date DATE        NOT NULL,
    card_holder     VARCHAR(30) NOT NULL,
    check_number    VARCHAR(3)  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE transactions
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    sender_id        INT            NOT NULL,
    recipient_id     INT            NOT NULL,
    amount           DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE
);