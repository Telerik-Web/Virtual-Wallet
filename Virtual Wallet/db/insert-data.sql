INSERT INTO users (first_name, last_name, username, password, email, phone, balance, isAdmin, isBlocked, account_verified)
VALUES
    ('John', 'Doe', 'johndoe', '$2a$12$XosuYXwzW.KIKZz4n5jwD.BZmCMn/UbWf4zbaCBesSesjMrRckGVC', 'john.doe@example.com', '1234567890', 1000, true, false, TRUE),
    ('Alice', 'Smith','alicesmith' , '$2a$12$JgwUmATgYxAFY6gKpI6wYOFs9xc03X5hplYOMKWWQMxaP1anzJNP2', 'alice.smith@example.com', '2345678901', 1000, false, false, TRUE),
    ('Bob', 'Johnson', 'bobjohnson', '$2a$12$YDawJBBfwAeAVYzkaDOvg.Cp7iSeb.ZTLQ.GDBQ1krC6z0zEMunvm', 'bob.johnson@example.com', '3456789012', 1000, false, false, TRUE),
    ('Emma', 'Brown', 'emmabrownN@!2', '$2a$12$Zrj5fXiHx3kk1.w1oIXkEeHje3x9O/4LGwHQDbNULPeHa3uS82fQO', 'emma.brown@example.com', '4567890123', 1000, false, true, TRUE),
    ('Michael', 'Davis', 'mikedavis', '$2a$12$HcJSg.nFI0nnlExQk2VB/elozQCyBuSmAWg7j5IlYWp3x5ks9.kma', 'mike.davis@example.com', '5678901234', 1500, false, false, TRUE),
    ('Sophia', 'Wilson', 'sophiawilson', '$2a$12$MrvIvY7T3aPRi71BpYcioe4WiIdZZHI/yfGpl8znWXLd9vG30EWp6', 'sophia.wilson@example.com', '6789012345', 2000, false, false, TRUE),
    ('James', 'Miller', 'jamesmiller', '$2a$12$/ux0EEdphkMw6tA9eAiRfuAU/Xdl3oQ4VxkJ/sIiIo25cgwgIWgY.', 'james.miller@example.com', '7890123456', 500, true, false, TRUE),
    ('Olivia', 'Taylor', 'oliviataylor', '$2a$12$P7sjvVtEtNNSN4eEK14pZeFOs6SE7X1c8v.APUUXv7xMMVPhhW3j.', 'olivia.taylor@example.com', '8901234567', 3000, false, false, TRUE),
    ('William', 'Anderson', 'williamanderson', '$2a$12$Slqfkl4cCLX651FHd6aMDe7tSnvdxMqWxzijpZU.fR3fMg.kf0AFi', 'william.anderson@example.com', '9012345678', 2500, false, true, TRUE),
    ('Ava', 'Thomas', 'avathomas', '$2a$12$qooPGAZChSkYGYLiZM2aN.n/lMKq0rsmF7seKZuscl7VhzeMJ4aHq', 'ava.thomas@example.com', '0123456789', 1200, false, false, TRUE);

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