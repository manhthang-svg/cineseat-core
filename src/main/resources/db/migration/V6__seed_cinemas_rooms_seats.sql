-- Seed Cinemas
INSERT INTO cinemas (id, name, address, deleted)
VALUES
    (1, 'CineVault Landmark 81', 'Tầng B1, Vincom Center Landmark 81, 720A Điện Biên Phủ, Phường 22, Bình Thạnh, TP. Hồ Chí Minh', FALSE),
    (2, 'CineVault Megamall Thảo Điền', 'Tầng 5, Vincom Mega Mall Thảo Điền, 161 Xa Lộ Hà Nội, Thảo Điền, TP. Thủ Đức, TP. Hồ Chí Minh', FALSE),
    (3, 'CineVault Royal City Hà Nội', 'Tầng B2, Vincom Mega Mall Royal City, 72A Nguyễn Trãi, Thượng Đình, Thanh Xuân, Hà Nội', FALSE),
    (4, 'CineVault Dragon Bridge Đà Nẵng', 'Tầng 4, TTTM Vincom Plaza Đà Nẵng, 910A Ngô Quyền, An Hải Bắc, Sơn Trà, Đà Nẵng', FALSE);

-- Seed Rooms
INSERT INTO rooms (id, name, cinema_id, deleted)
VALUES
    (1, 'Cinema 1 (IMAX Laser)', 1, FALSE),
    (2, 'Cinema 2 (Dolby Atmos)', 1, FALSE),
    (3, 'Cinema 3 (Gold Class)', 1, FALSE),
    (4, 'Cinema 1 (Standard)', 2, FALSE),
    (5, 'Cinema 2 (4DX)', 2, FALSE),
    (6, 'Cinema 1 (IMAX Laser)', 3, FALSE),
    (7, 'Cinema 2 (Premium)', 3, FALSE),
    (8, 'Cinema 1 (Standard)', 4, FALSE);

-- Seed Seats for Cinema 1 (IMAX Laser) - Room ID 1
INSERT INTO seats (row_label, seat_number, seat_type, room_id, deleted)
VALUES
    -- Row A (Regular)
    ('A', 1, 'REGULAR', 1, FALSE), ('A', 2, 'REGULAR', 1, FALSE), ('A', 3, 'REGULAR', 1, FALSE), ('A', 4, 'REGULAR', 1, FALSE),
    ('A', 5, 'REGULAR', 1, FALSE), ('A', 6, 'REGULAR', 1, FALSE), ('A', 7, 'REGULAR', 1, FALSE), ('A', 8, 'REGULAR', 1, FALSE),
    -- Row B (Regular)
    ('B', 1, 'REGULAR', 1, FALSE), ('B', 2, 'REGULAR', 1, FALSE), ('B', 3, 'REGULAR', 1, FALSE), ('B', 4, 'REGULAR', 1, FALSE),
    ('B', 5, 'REGULAR', 1, FALSE), ('B', 6, 'REGULAR', 1, FALSE), ('B', 7, 'REGULAR', 1, FALSE), ('B', 8, 'REGULAR', 1, FALSE),
    -- Row C (Regular)
    ('C', 1, 'REGULAR', 1, FALSE), ('C', 2, 'REGULAR', 1, FALSE), ('C', 3, 'REGULAR', 1, FALSE), ('C', 4, 'REGULAR', 1, FALSE),
    ('C', 5, 'REGULAR', 1, FALSE), ('C', 6, 'REGULAR', 1, FALSE), ('C', 7, 'REGULAR', 1, FALSE), ('C', 8, 'REGULAR', 1, FALSE),
    -- Row D (VIP)
    ('D', 1, 'VIP', 1, FALSE), ('D', 2, 'VIP', 1, FALSE), ('D', 3, 'VIP', 1, FALSE), ('D', 4, 'VIP', 1, FALSE),
    ('D', 5, 'VIP', 1, FALSE), ('D', 6, 'VIP', 1, FALSE), ('D', 7, 'VIP', 1, FALSE), ('D', 8, 'VIP', 1, FALSE),
    -- Row E (VIP)
    ('E', 1, 'VIP', 1, FALSE), ('E', 2, 'VIP', 1, FALSE), ('E', 3, 'VIP', 1, FALSE), ('E', 4, 'VIP', 1, FALSE),
    ('E', 5, 'VIP', 1, FALSE), ('E', 6, 'VIP', 1, FALSE), ('E', 7, 'VIP', 1, FALSE), ('E', 8, 'VIP', 1, FALSE),
    -- Row F (Couple)
    ('F', 1, 'COUPLE', 1, FALSE), ('F', 2, 'COUPLE', 1, FALSE), ('F', 3, 'COUPLE', 1, FALSE), ('F', 4, 'COUPLE', 1, FALSE);

-- Seed Seats for Cinema 2 (Dolby Atmos) - Room ID 2
INSERT INTO seats (row_label, seat_number, seat_type, room_id, deleted)
VALUES
    ('A', 1, 'REGULAR', 2, FALSE), ('A', 2, 'REGULAR', 2, FALSE), ('A', 3, 'REGULAR', 2, FALSE), ('A', 4, 'REGULAR', 2, FALSE),
    ('A', 5, 'REGULAR', 2, FALSE), ('A', 6, 'REGULAR', 2, FALSE),
    ('B', 1, 'REGULAR', 2, FALSE), ('B', 2, 'REGULAR', 2, FALSE), ('B', 3, 'REGULAR', 2, FALSE), ('B', 4, 'REGULAR', 2, FALSE),
    ('B', 5, 'REGULAR', 2, FALSE), ('B', 6, 'REGULAR', 2, FALSE),
    ('C', 1, 'VIP', 2, FALSE), ('C', 2, 'VIP', 2, FALSE), ('C', 3, 'VIP', 2, FALSE), ('C', 4, 'VIP', 2, FALSE),
    ('C', 5, 'VIP', 2, FALSE), ('C', 6, 'VIP', 2, FALSE),
    ('D', 1, 'COUPLE', 2, FALSE), ('D', 2, 'COUPLE', 2, FALSE);

-- Seed Seats for Cinema 1 (Standard Thảo Điền) - Room ID 4
INSERT INTO seats (row_label, seat_number, seat_type, room_id, deleted)
VALUES
    ('A', 1, 'REGULAR', 4, FALSE), ('A', 2, 'REGULAR', 4, FALSE), ('A', 3, 'REGULAR', 4, FALSE), ('A', 4, 'REGULAR', 4, FALSE),
    ('B', 1, 'REGULAR', 4, FALSE), ('B', 2, 'REGULAR', 4, FALSE), ('B', 3, 'REGULAR', 4, FALSE), ('B', 4, 'REGULAR', 4, FALSE),
    ('C', 1, 'VIP', 4, FALSE), ('C', 2, 'VIP', 4, FALSE), ('C', 3, 'VIP', 4, FALSE), ('C', 4, 'VIP', 4, FALSE),
    ('D', 1, 'COUPLE', 4, FALSE), ('D', 2, 'COUPLE', 4, FALSE);

-- Seed Seats for Cinema 1 (IMAX Royal City) - Room ID 6
INSERT INTO seats (row_label, seat_number, seat_type, room_id, deleted)
VALUES
    ('A', 1, 'REGULAR', 6, FALSE), ('A', 2, 'REGULAR', 6, FALSE), ('A', 3, 'REGULAR', 6, FALSE), ('A', 4, 'REGULAR', 6, FALSE),
    ('B', 1, 'VIP', 6, FALSE), ('B', 2, 'VIP', 6, FALSE), ('B', 3, 'VIP', 6, FALSE), ('B', 4, 'VIP', 6, FALSE),
    ('C', 1, 'COUPLE', 6, FALSE), ('C', 2, 'COUPLE', 6, FALSE);
