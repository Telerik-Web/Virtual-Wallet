CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(16)  NOT NULL,
    last_name  VARCHAR(16)  NOT NULL,
    username   VARCHAR(20)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(10)  NOT NULL UNIQUE,
    photo      LONGBLOB,
    isAdmin    BOOLEAN      NOT NULL,
    isBlocked  BOOLEAN      NOT NULL
);

CREATE TABLE cards
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number     VARCHAR(16) NOT NULL UNIQUE,
    expiration_date DATE        NOT NULL,
    check_number    VARCHAR(3)  NOT NULL,
    user_id         BIGINT      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE users_cards
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);