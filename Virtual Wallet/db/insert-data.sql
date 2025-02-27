INSERT INTO users (first_name, last_name, username, password, email, phone, photo, isAdmin, isBlocked)
VALUES ('John', 'Doe', 'johndoe', 'pass1', 'john.doe@example.com', '1234567890', NULL, true, false),
       ('Alice', 'Smith', 'alicesmith', 'pass2', 'alice.smith@example.com', '2345678901', NULL, false, false),
       ('Bob', 'Johnson', 'bobjohnson', 'pass3', 'bob.johnson@example.com', '3456789012', NULL, false, false),
       ('Emma', 'Brown', 'emmabrown', 'pass4', 'emma.brown@example.com', '4567890123', NULL, false, true);


INSERT INTO cards (card_number, expiration_date, check_number, user_id)
VALUES ('1111222233334444', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '123', 1),
       ('5555666677778888', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '456', 2),
       ('9999000011112222', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '789', 2),
       ('3333444455556666', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '321', 3),
       ('7777888899990000', DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '654', 4);

INSERT INTO users_cards (card_id, user_id)
VALUES (1, 2),
       (2, 2),
       (1, 4);