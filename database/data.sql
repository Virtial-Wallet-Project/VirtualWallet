INSERT INTO users (username, password, email, phone_number, balance, is_admin, is_blocked) VALUES
('ivan1999', 'Ivan1999!', 'ivan@gmail.com', '0888359305', 100.00, FALSE, FALSE),
('vik050302', 'VikGeorg05@', 'vik@abv.bg', '0889345123', 25.00, FALSE, FALSE),
('georgiMar', 'GoshoMarinov00#', 'gosho@gmail.com', '0887123456', 1000.00, TRUE, FALSE),
('venciVenc', 'Venc1989$', 'venci@gmail.com', '0888654321', 2000.00, FALSE, TRUE),
('martinBlag', 'MartoBlag03%', 'marto123@gmail.com', '0887987654', 700.00, FALSE, FALSE),
('gergana03', 'Gergana03^', 'gerii@abv.bg', '0887765432', 200.00, FALSE, FALSE),
('drago3636', 'Drago6363&', 'drago@abv.bg', '0887555555', 50.00, FALSE, FALSE),
('marinMar', 'MartiMari05*', 'marinov@gmail.com', '0887234567', 500.00, TRUE, FALSE),
('simonaIvn', 'SimonaIvn01(', 'moni@gmail.com', '0887878787', 1500.00, FALSE, TRUE),
('blagoi02', 'Blagoi2002)', 'blago@gmail.com', '0887698523', 300.00, FALSE, FALSE),
('ivanelaBoris', 'Ivanela99-', 'ivanela@gmail.com', '0887456123', 750.00, FALSE, FALSE),
('chavo1221', 'Chavo2112_', 'chavo@gmail.com', '0887999888', 75.00, FALSE, FALSE),
('anna-mary', 'Anna-Mary88=', 'anna-mary@abv.bg', '0887123123', 600.00, TRUE, FALSE),
('emilAng', 'EmilAng66+', 'emo@gmail.com', '0887543210', 2500.00, FALSE, TRUE),
('raya06', 'Raya2006!', 'raya@gmail.com', '0887222333', 900.00, FALSE, FALSE);

INSERT INTO credit_cards (user_id, card_number, expiration_date, card_holder, check_number) VALUES

(1, '4532879645237890', '2026-12-11', 'Ivan Angelov', '123'),
(2, '5276341290876543', '2025-06-30', 'Viktor Ivanov', '456'),
(3, '3791234567890123', '2027-08-15', 'Georgi Marinov', '789'),
(3, '6011567823456789', '2026-03-20', 'Georgi Marinov', '321'),
(5, '4567890123456782', '2027-07-14', 'Martin Blagoev', '654'),
(6, '5140765432109876', '2026-12-31', 'Gergana Boneva', '671'),
(8, '3742198765432109', '2025-08-22', 'Marin Marinov', '425'),
(8, '6011223344556677', '2027-08-15', 'Marin Marinov', '345'),
(9, '4539876543210987', '2026-03-20', 'Simona Ivanova', '752'),
(11, '5290112233445566', '2028-12-28', 'Ivanela Boris', '543'),
(11, '6011998877665544', '2026-05-17', 'Ivanela Boris', '863'),
(11, '3794564321098765', '2025-06-30', 'Ivanela Boris', '594'),
(13, '4531234567890123', '2027-08-15', 'Anna Mary', '318'),
(14, '5278901234567890', '2026-04-20', 'Emil Angelov', '343'),
(15, '3749876543210987', '2029-11-10', 'Raya Georgieva', '876');

INSERT INTO transactions (sender_id, recipient_id, amount) VALUES
(1, 2, 50.75),
(2, 3, 120.00),
(3, 5, 200.50),
(5, 1, 15.99),
(2, 4, 75.25);
