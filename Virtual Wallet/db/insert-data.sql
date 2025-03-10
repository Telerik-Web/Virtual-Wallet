INSERT INTO users (first_name, last_name, username, password, email, phone, balance, photo, isAdmin, isBlocked, account_verified)
VALUES ('John', 'Doe', 'johndoe', 'pass1', 'john.doe@example.com', '1234567890', '1000', NULL, true, false, TRUE),
       ('Alice', 'Smith', 'alicesmith', 'pass2', 'alice.smith@example.com', '2345678901', '1000', NULL, false, false, TRUE),
       ('Bob', 'Johnson', 'bobjohnson', 'pass3', 'bob.johnson@example.com', '3456789012', '1000', NULL, false, false, TRUE),
       ('Emma', 'Brown', 'emmabrownN@!2', 'emmabrownN@!2', 'emma.brown@example.com', '4567890123', '1000', NULL, false,
        true, TRUE);


INSERT INTO cards (card_number, expiration_date, check_number, user_id)
VALUES ('1111222233334444', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '123', 1),
       ('5555666677778888', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '456', 4),
       ('9999000011112222', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '789', 4),
       ('3333444455556666', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '321', 4),
       ('7777888899990000', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '654', 4);


INSERT INTO transactions (sender_id, recipient_id, amount, status, created_at)
VALUES (1, 2, 100, 'PENDING', CURRENT_TIMESTAMP),
       (2, 3, 200, 'PENDING', CURRENT_TIMESTAMP),
       (3, 2, 400, 'COMPLETED', CURRENT_TIMESTAMP),
       (1, 2, 10.10, 'FAILED', CURRENT_TIMESTAMP);

INSERT INTO deposits (card_id, amount, created_at)
VALUES (2, 20, CURRENT_TIMESTAMP),
       (2, 30, CURRENT_TIMESTAMP),
       (4, 200, CURRENT_TIMESTAMP),
       (5, 2000, CURRENT_TIMESTAMP);