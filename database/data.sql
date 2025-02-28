INSERT INTO users (username, password, email, phone_number, is_admin, is_blocked) VALUES
('user1', 'hashed_password1', 'user1@example.com', '1234567890', FALSE, FALSE),
('user2', 'hashed_password2', 'user2@example.com', '0987654321', FALSE, FALSE),
('admin1', 'hashed_admin1', 'admin@example.com', '1112223333', TRUE, FALSE),
('blocked1', 'hashed_password3', 'blocked@example.com', '2223334444', FALSE, TRUE),
('user3', 'hashed_password4', 'user3@example.com', '3334445555', FALSE, FALSE);

INSERT INTO credit_cards (user_id, card_number, expiration_date, card_holder, check_number) VALUES

(1, '4111111111111111', '2026-12-31', 'User One', '123'),
(2, '5500000000000004', '2025-06-30', 'User Two', '456'),
(3, '340000000000009', '2027-08-15', 'Admin One', '789'),
(4, '30000000000004', '2026-03-20', 'Blocked User', '321'),
(5, '6011000000000004', '2024-11-10', 'User Three', '654');

INSERT INTO transactions (sender_id, recipient_id, amount) VALUES
(1, 2, 50.75),
(2, 3, 120.00),
(3, 5, 200.50),
(5, 1, 15.99),
(2, 4, 75.25);
