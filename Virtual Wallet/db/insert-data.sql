INSERT INTO users (first_name, last_name, username, password, email, phone, balance, isAdmin, isBlocked, account_verified)
VALUES
    ('John', 'Doe', 'johndoe', 'pass1', 'john.doe@example.com', '1234567890', 1000, true, false, TRUE),
    ('Alice', 'Smith', 'alicesmith', 'pass2', 'alice.smith@example.com', '2345678901', 1000, false, false, TRUE),
    ('Bob', 'Johnson', 'bobjohnson', 'pass3', 'bob.johnson@example.com', '3456789012', 1000, false, false, TRUE),
    ('Emma', 'Brown', 'emmabrownN@!2', 'emmabrownN@!2', 'emma.brown@example.com', '4567890123', 1000, false, true, TRUE),
    ('Michael', 'Davis', 'mikedavis', 'pass4', 'mike.davis@example.com', '5678901234', 1500, false, false, TRUE),
    ('Sophia', 'Wilson', 'sophiawilson', 'pass5', 'sophia.wilson@example.com', '6789012345', 2000, false, false, TRUE),
    ('James', 'Miller', 'jamesmiller', 'pass6', 'james.miller@example.com', '7890123456', 500, true, false, TRUE),
    ('Olivia', 'Taylor', 'oliviataylor', 'pass7', 'olivia.taylor@example.com', '8901234567', 3000, false, false, TRUE),
    ('William', 'Anderson', 'williamanderson', 'pass8', 'william.anderson@example.com', '9012345678', 2500, false, true, TRUE),
    ('Ava', 'Thomas', 'avathomas', 'pass9', 'ava.thomas@example.com', '0123456789', 1200, false, false, TRUE);

-- Insert more cards
INSERT INTO cards (card_number, expiration_date, check_number, user_id)
VALUES
    ('1111222233334444', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '123', 1),
    ('5555666677778888', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '456', 4),
    ('9999000011112222', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '789', 4),
    ('3333444455556666', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '321', 4),
    ('7777888899990000', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '654', 4),
    ('1234567890123456', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '987', 2),
    ('2345678901234567', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '876', 3),
    ('3456789012345678', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '765', 5),
    ('4567890123456789', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '654', 6),
    ('5678901234567890', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '543', 7);

-- Insert more transactions
INSERT INTO transactions (sender_id, recipient_id, amount, status, created_at)
VALUES
    (1, 2, 100, 'PENDING', CURRENT_TIMESTAMP),
    (2, 3, 200, 'PENDING', CURRENT_TIMESTAMP),
    (3, 2, 400, 'COMPLETED', CURRENT_TIMESTAMP),
    (1, 2, 10.10, 'FAILED', CURRENT_TIMESTAMP),
    (4, 5, 50, 'COMPLETED', CURRENT_TIMESTAMP),
    (5, 6, 150, 'PENDING', CURRENT_TIMESTAMP),
    (6, 7, 300, 'COMPLETED', CURRENT_TIMESTAMP),
    (7, 8, 200, 'FAILED', CURRENT_TIMESTAMP),
    (8, 9, 1000, 'COMPLETED', CURRENT_TIMESTAMP),
    (9, 10, 500, 'PENDING', CURRENT_TIMESTAMP);

-- Insert more deposits
INSERT INTO deposits (card_id, amount, created_at)
VALUES
    (2, 20, CURRENT_TIMESTAMP),
    (2, 30, CURRENT_TIMESTAMP),
    (4, 200, CURRENT_TIMESTAMP),
    (5, 2000, CURRENT_TIMESTAMP),
    (6, 50, CURRENT_TIMESTAMP),
    (7, 100, CURRENT_TIMESTAMP),
    (8, 150, CURRENT_TIMESTAMP),
    (9, 300, CURRENT_TIMESTAMP),
    (10, 250, CURRENT_TIMESTAMP),
    (1, 500, CURRENT_TIMESTAMP);