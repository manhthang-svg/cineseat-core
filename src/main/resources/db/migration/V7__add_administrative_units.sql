-- ==========================================================
-- V7: Add Administrative Units & Update Cinema Address Model
-- ==========================================================

-- 1. Create provinces table
CREATE TABLE provinces (
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    unit_type VARCHAR(50) NULL,
    PRIMARY KEY (code)
);

-- 2. Create districts table
CREATE TABLE districts (
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    unit_type VARCHAR(50) NULL,
    province_code VARCHAR(20) NOT NULL,
    PRIMARY KEY (code),
    CONSTRAINT fk_districts_province FOREIGN KEY (province_code) REFERENCES provinces(code)
);

CREATE INDEX idx_districts_province_code ON districts(province_code);

-- 3. Create wards table
CREATE TABLE wards (
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    unit_type VARCHAR(50) NULL,
    district_code VARCHAR(20) NOT NULL,
    PRIMARY KEY (code),
    CONSTRAINT fk_wards_district FOREIGN KEY (district_code) REFERENCES districts(code)
);

CREATE INDEX idx_wards_district_code ON wards(district_code);

-- 4. Seed Provinces
INSERT INTO provinces (code, name, full_name, unit_type) VALUES
('01', 'Hà Nội', 'Thành phố Hà Nội', 'Thành phố trực thuộc Trung ương'),
('79', 'Hồ Chí Minh', 'Thành phố Hồ Chí Minh', 'Thành phố trực thuộc Trung ương'),
('48', 'Đà Nẵng', 'Thành phố Đà Nẵng', 'Thành phố trực thuộc Trung ương'),
('31', 'Hải Phòng', 'Thành phố Hải Phòng', 'Thành phố trực thuộc Trung ương'),
('92', 'Cần Thơ', 'Thành phố Cần Thơ', 'Thành phố trực thuộc Trung ương'),
('46', 'Thừa Thiên Huế', 'Tỉnh Thừa Thiên Huế', 'Tỉnh'),
('56', 'Khánh Hòa', 'Tỉnh Khánh Hòa', 'Tỉnh'),
('75', 'Đồng Nai', 'Tỉnh Đồng Nai', 'Tỉnh'),
('74', 'Bình Dương', 'Tỉnh Bình Dương', 'Tỉnh'),
('77', 'Bà Rịa - Vũng Tàu', 'Tỉnh Bà Rịa - Vũng Tàu', 'Tỉnh');

-- 5. Seed Districts
INSERT INTO districts (code, name, full_name, unit_type, province_code) VALUES
-- TP. Hồ Chí Minh (79)
('765', 'Bình Thạnh', 'Quận Bình Thạnh', 'Quận', '79'),
('769', 'Thủ Đức', 'Thành phố Thủ Đức', 'Thành phố thuộc thành phố trực thuộc Trung ương', '79'),
('760', 'Quận 1', 'Quận 1', 'Quận', '79'),
('770', 'Quận 3', 'Quận 3', 'Quận', '79'),
('778', 'Quận 7', 'Quận 7', 'Quận', '79'),
('771', 'Quận 10', 'Quận 10', 'Quận', '79'),
('768', 'Phú Nhuận', 'Quận Phú Nhuận', 'Quận', '79'),
('764', 'Gò Vấp', 'Quận Gò Vấp', 'Quận', '79'),
('766', 'Tân Bình', 'Quận Tân Bình', 'Quận', '79'),

-- Hà Nội (01)
('009', 'Thanh Xuân', 'Quận Thanh Xuân', 'Quận', '01'),
('005', 'Cầu Giấy', 'Quận Cầu Giấy', 'Quận', '01'),
('001', 'Hoàn Kiếm', 'Quận Hoàn Kiếm', 'Quận', '01'),
('002', 'Ba Đình', 'Quận Ba Đình', 'Quận', '01'),
('006', 'Đống Đa', 'Quận Đống Đa', 'Quận', '01'),
('007', 'Hai Bà Trưng', 'Quận Hai Bà Trưng', 'Quận', '01'),
('016', 'Hà Đông', 'Quận Hà Đông', 'Quận', '01'),
('019', 'Nam Từ Liêm', 'Quận Nam Từ Liêm', 'Quận', '01'),
('021', 'Bắc Từ Liêm', 'Quận Bắc Từ Liêm', 'Quận', '01'),

-- Đà Nẵng (48)
('492', 'Sơn Trà', 'Quận Sơn Trà', 'Quận', '48'),
('490', 'Hải Châu', 'Quận Hải Châu', 'Quận', '48'),
('491', 'Thanh Khê', 'Quận Thanh Khê', 'Quận', '48'),
('493', 'Ngũ Hành Sơn', 'Quận Ngũ Hành Sơn', 'Quận', '48'),
('494', 'Liên Chiểu', 'Quận Liên Chiểu', 'Quận', '48'),
('495', 'Cẩm Lệ', 'Quận Cẩm Lệ', 'Quận', '48');

-- 6. Seed Wards
INSERT INTO wards (code, name, full_name, unit_type, district_code) VALUES
-- Bình Thạnh (765)
('26830', 'Phường 22', 'Phường 22', 'Phường', '765'),
('26833', 'Phường 19', 'Phường 19', 'Phường', '765'),
('26836', 'Phường 21', 'Phường 21', 'Phường', '765'),
('26839', 'Phường 25', 'Phường 25', 'Phường', '765'),

-- TP. Thủ Đức (769)
('26874', 'Thảo Điền', 'Phường Thảo Điền', 'Phường', '769'),
('26877', 'An Phú', 'Phường An Phú', 'Phường', '769'),
('26880', 'An Khánh', 'Phường An Khánh', 'Phường', '769'),
('26883', 'Hiệp Phú', 'Phường Hiệp Phú', 'Phường', '769'),

-- Quận 1 (760)
('26734', 'Bến Nghé', 'Phường Bến Nghé', 'Phường', '760'),
('26737', 'Bến Thành', 'Phường Bến Thành', 'Phường', '760'),
('26740', 'Đa Kao', 'Phường Đa Kao', 'Phường', '760'),
('26743', 'Tân Định', 'Phường Tân Định', 'Phường', '760'),

-- Thanh Xuân (009)
('00340', 'Thượng Đình', 'Phường Thượng Đình', 'Phường', '009'),
('00343', 'Khương Mai', 'Phường Khương Mai', 'Phường', '009'),
('00346', 'Thanh Xuân Bắc', 'Phường Thanh Xuân Bắc', 'Phường', '009'),
('00349', 'Thanh Xuân Trung', 'Phường Thanh Xuân Trung', 'Phường', '009'),

-- Cầu Giấy (005)
('00160', 'Dịch Vọng Hậu', 'Phường Dịch Vọng Hậu', 'Phường', '005'),
('00163', 'Dịch Vọng', 'Phường Dịch Vọng', 'Phường', '005'),
('00166', 'Yên Hòa', 'Phường Yên Hòa', 'Phường', '005'),
('00169', 'Trung Hòa', 'Phường Trung Hòa', 'Phường', '005'),

-- Hoàn Kiếm (001)
('00001', 'Hàng Bạc', 'Phường Hàng Bạc', 'Phường', '001'),
('00004', 'Tràng Tiền', 'Phường Tràng Tiền', 'Phường', '001'),
('00007', 'Hàng Trống', 'Phường Hàng Trống', 'Phường', '001'),

-- Sơn Trà (492)
('20242', 'An Hải Bắc', 'Phường An Hải Bắc', 'Phường', '492'),
('20245', 'An Hải Đông', 'Phường An Hải Đông', 'Phường', '492'),
('20248', 'Phước Mỹ', 'Phường Phước Mỹ', 'Phường', '492'),

-- Hải Châu (490)
('20194', 'Hải Châu 1', 'Phường Hải Châu 1', 'Phường', '490'),
('20197', 'Hải Châu 2', 'Phường Hải Châu 2', 'Phường', '490'),
('20200', 'Thạch Thang', 'Phường Thạch Thang', 'Phường', '490');

-- 7. Update cinemas table to support structured hierarchical address
ALTER TABLE cinemas
    ADD COLUMN province_code VARCHAR(20) NULL AFTER name,
    ADD COLUMN district_code VARCHAR(20) NULL AFTER province_code,
    ADD COLUMN ward_code VARCHAR(20) NULL AFTER district_code,
    ADD COLUMN detail_address VARCHAR(255) NULL AFTER ward_code;

-- 8. Backfill existing seed cinemas data
UPDATE cinemas SET
    province_code = '79',
    district_code = '765',
    ward_code = '26830',
    detail_address = 'Tầng B1, Vincom Center Landmark 81, 720A Điện Biên Phủ',
    address = 'Tầng B1, Vincom Center Landmark 81, 720A Điện Biên Phủ, Phường 22, Quận Bình Thạnh, Thành phố Hồ Chí Minh'
WHERE id = 1;

UPDATE cinemas SET
    province_code = '79',
    district_code = '769',
    ward_code = '26874',
    detail_address = 'Tầng 5, Vincom Mega Mall Thảo Điền, 161 Xa Lộ Hà Nội',
    address = 'Tầng 5, Vincom Mega Mall Thảo Điền, 161 Xa Lộ Hà Nội, Phường Thảo Điền, Thành phố Thủ Đức, Thành phố Hồ Chí Minh'
WHERE id = 2;

UPDATE cinemas SET
    province_code = '01',
    district_code = '009',
    ward_code = '00340',
    detail_address = 'Tầng B2, Vincom Mega Mall Royal City, 72A Nguyễn Trãi',
    address = 'Tầng B2, Vincom Mega Mall Royal City, 72A Nguyễn Trãi, Phường Thượng Đình, Quận Thanh Xuân, Thành phố Hà Nội'
WHERE id = 3;

UPDATE cinemas SET
    province_code = '48',
    district_code = '492',
    ward_code = '20242',
    detail_address = 'Tầng 4, TTTM Vincom Plaza Đà Nẵng, 910A Ngô Quyền',
    address = 'Tầng 4, TTTM Vincom Plaza Đà Nẵng, 910A Ngô Quyền, Phường An Hải Bắc, Quận Sơn Trà, Thành phố Đà Nẵng'
WHERE id = 4;

-- 9. Add foreign key constraints & indexes
ALTER TABLE cinemas
    ADD CONSTRAINT fk_cinemas_province FOREIGN KEY (province_code) REFERENCES provinces(code),
    ADD CONSTRAINT fk_cinemas_district FOREIGN KEY (district_code) REFERENCES districts(code),
    ADD CONSTRAINT fk_cinemas_ward FOREIGN KEY (ward_code) REFERENCES wards(code);

CREATE INDEX idx_cinemas_province_code ON cinemas(province_code);
CREATE INDEX idx_cinemas_district_code ON cinemas(district_code);
CREATE INDEX idx_cinemas_ward_code ON cinemas(ward_code);
