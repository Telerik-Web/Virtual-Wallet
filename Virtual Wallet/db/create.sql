CREATE TABLE users
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name         VARCHAR(16)  NOT NULL,
    last_name          VARCHAR(16)  NOT NULL,
    username           VARCHAR(20)  NOT NULL UNIQUE,
    password           VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL UNIQUE,
    phone              VARCHAR(10)  NOT NULL UNIQUE,
    balance            VARCHAR(255) NOT NULL,
    photo              VARCHAR(320)   DEFAULT 'img/default-profile-pic.png',
    isAdmin            BOOLEAN      NOT NULL,
    isBlocked          BOOLEAN      NOT NULL,
    verification_token VARCHAR(255) UNIQUE,
    account_verified   BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE cards
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number     VARCHAR(16) NOT NULL UNIQUE,
    expiration_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 5 YEAR),
    check_number    VARCHAR(3)  NOT NULL,
    user_id         BIGINT      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE transactions
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id    BIGINT                                  NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    recipient_id BIGINT                                  NOT NULL,
    FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE,
    amount       DECIMAL(10, 2)                          NOT NULL,
    status       ENUM ('PENDING', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMP                                        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE deposits
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id    BIGINT         NOT NULL,
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE,
    amount     DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
