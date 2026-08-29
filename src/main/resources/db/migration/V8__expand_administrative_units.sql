-- ==========================================================
-- V8: Expand Administrative Units - All 63 Provinces of Vietnam
-- ==========================================================

-- Add remaining 53 provinces (V7 already seeded 10)
INSERT INTO provinces (code, name, full_name, unit_type) VALUES
('02', 'Hà Giang', 'Tỉnh Hà Giang', 'Tỉnh'),
('04', 'Cao Bằng', 'Tỉnh Cao Bằng', 'Tỉnh'),
('06', 'Bắc Kạn', 'Tỉnh Bắc Kạn', 'Tỉnh'),
('08', 'Tuyên Quang', 'Tỉnh Tuyên Quang', 'Tỉnh'),
('10', 'Lào Cai', 'Tỉnh Lào Cai', 'Tỉnh'),
('11', 'Điện Biên', 'Tỉnh Điện Biên', 'Tỉnh'),
('12', 'Lai Châu', 'Tỉnh Lai Châu', 'Tỉnh'),
('14', 'Sơn La', 'Tỉnh Sơn La', 'Tỉnh'),
('15', 'Yên Bái', 'Tỉnh Yên Bái', 'Tỉnh'),
('17', 'Hòa Bình', 'Tỉnh Hòa Bình', 'Tỉnh'),
('19', 'Thái Nguyên', 'Tỉnh Thái Nguyên', 'Tỉnh'),
('20', 'Lạng Sơn', 'Tỉnh Lạng Sơn', 'Tỉnh'),
('22', 'Quảng Ninh', 'Tỉnh Quảng Ninh', 'Tỉnh'),
('24', 'Bắc Giang', 'Tỉnh Bắc Giang', 'Tỉnh'),
('25', 'Phú Thọ', 'Tỉnh Phú Thọ', 'Tỉnh'),
('26', 'Vĩnh Phúc', 'Tỉnh Vĩnh Phúc', 'Tỉnh'),
('27', 'Bắc Ninh', 'Tỉnh Bắc Ninh', 'Tỉnh'),
('30', 'Hải Dương', 'Tỉnh Hải Dương', 'Tỉnh'),
('33', 'Hưng Yên', 'Tỉnh Hưng Yên', 'Tỉnh'),
('34', 'Thái Bình', 'Tỉnh Thái Bình', 'Tỉnh'),
('35', 'Hà Nam', 'Tỉnh Hà Nam', 'Tỉnh'),
('36', 'Nam Định', 'Tỉnh Nam Định', 'Tỉnh'),
('37', 'Ninh Bình', 'Tỉnh Ninh Bình', 'Tỉnh'),
('38', 'Thanh Hóa', 'Tỉnh Thanh Hóa', 'Tỉnh'),
('40', 'Nghệ An', 'Tỉnh Nghệ An', 'Tỉnh'),
('42', 'Hà Tĩnh', 'Tỉnh Hà Tĩnh', 'Tỉnh'),
('44', 'Quảng Bình', 'Tỉnh Quảng Bình', 'Tỉnh'),
('45', 'Quảng Trị', 'Tỉnh Quảng Trị', 'Tỉnh'),
('49', 'Quảng Nam', 'Tỉnh Quảng Nam', 'Tỉnh'),
('51', 'Quảng Ngãi', 'Tỉnh Quảng Ngãi', 'Tỉnh'),
('52', 'Bình Định', 'Tỉnh Bình Định', 'Tỉnh'),
('54', 'Phú Yên', 'Tỉnh Phú Yên', 'Tỉnh'),
('58', 'Ninh Thuận', 'Tỉnh Ninh Thuận', 'Tỉnh'),
('60', 'Bình Thuận', 'Tỉnh Bình Thuận', 'Tỉnh'),
('62', 'Kon Tum', 'Tỉnh Kon Tum', 'Tỉnh'),
('64', 'Gia Lai', 'Tỉnh Gia Lai', 'Tỉnh'),
('66', 'Đắk Lắk', 'Tỉnh Đắk Lắk', 'Tỉnh'),
('67', 'Đắk Nông', 'Tỉnh Đắk Nông', 'Tỉnh'),
('68', 'Lâm Đồng', 'Tỉnh Lâm Đồng', 'Tỉnh'),
('70', 'Bình Phước', 'Tỉnh Bình Phước', 'Tỉnh'),
('72', 'Tây Ninh', 'Tỉnh Tây Ninh', 'Tỉnh'),
('80', 'Long An', 'Tỉnh Long An', 'Tỉnh'),
('82', 'Tiền Giang', 'Tỉnh Tiền Giang', 'Tỉnh'),
('83', 'Bến Tre', 'Tỉnh Bến Tre', 'Tỉnh'),
('84', 'Trà Vinh', 'Tỉnh Trà Vinh', 'Tỉnh'),
('86', 'Vĩnh Long', 'Tỉnh Vĩnh Long', 'Tỉnh'),
('87', 'Đồng Tháp', 'Tỉnh Đồng Tháp', 'Tỉnh'),
('89', 'An Giang', 'Tỉnh An Giang', 'Tỉnh'),
('91', 'Kiên Giang', 'Tỉnh Kiên Giang', 'Tỉnh'),
('93', 'Hậu Giang', 'Tỉnh Hậu Giang', 'Tỉnh'),
('94', 'Sóc Trăng', 'Tỉnh Sóc Trăng', 'Tỉnh'),
('95', 'Bạc Liêu', 'Tỉnh Bạc Liêu', 'Tỉnh'),
('96', 'Cà Mau', 'Tỉnh Cà Mau', 'Tỉnh');

-- Add remaining districts for existing provinces (V7 only seeded select few)
INSERT INTO districts (code, name, full_name, unit_type, province_code) VALUES
-- TP. Hồ Chí Minh - thêm các quận còn lại
('761', 'Quận 12', 'Quận 12', 'Quận', '79'),
('772', 'Quận 11', 'Quận 11', 'Quận', '79'),
('773', 'Quận 4', 'Quận 4', 'Quận', '79'),
('774', 'Quận 5', 'Quận 5', 'Quận', '79'),
('775', 'Quận 6', 'Quận 6', 'Quận', '79'),
('776', 'Quận 8', 'Quận 8', 'Quận', '79'),
('777', 'Bình Tân', 'Quận Bình Tân', 'Quận', '79'),
('767', 'Tân Phú', 'Quận Tân Phú', 'Quận', '79'),
('783', 'Củ Chi', 'Huyện Củ Chi', 'Huyện', '79'),
('784', 'Hóc Môn', 'Huyện Hóc Môn', 'Huyện', '79'),
('785', 'Bình Chánh', 'Huyện Bình Chánh', 'Huyện', '79'),
('786', 'Nhà Bè', 'Huyện Nhà Bè', 'Huyện', '79'),
('787', 'Cần Giờ', 'Huyện Cần Giờ', 'Huyện', '79'),

-- Hà Nội - thêm các quận/huyện còn lại
('003', 'Tây Hồ', 'Quận Tây Hồ', 'Quận', '01'),
('004', 'Long Biên', 'Quận Long Biên', 'Quận', '01'),
('008', 'Hoàng Mai', 'Quận Hoàng Mai', 'Quận', '01'),
('017', 'Sơn Tây', 'Thị xã Sơn Tây', 'Thị xã', '01'),
('018', 'Ba Vì', 'Huyện Ba Vì', 'Huyện', '01'),
('020', 'Mê Linh', 'Huyện Mê Linh', 'Huyện', '01'),
('250', 'Đông Anh', 'Huyện Đông Anh', 'Huyện', '01'),
('268', 'Gia Lâm', 'Huyện Gia Lâm', 'Huyện', '01'),

-- Đà Nẵng - thêm huyện còn lại
('497', 'Hòa Vang', 'Huyện Hòa Vang', 'Huyện', '48'),

-- Hải Phòng
('303', 'Hồng Bàng', 'Quận Hồng Bàng', 'Quận', '31'),
('304', 'Ngô Quyền', 'Quận Ngô Quyền', 'Quận', '31'),
('305', 'Lê Chân', 'Quận Lê Chân', 'Quận', '31'),
('306', 'Hải An', 'Quận Hải An', 'Quận', '31'),
('307', 'Kiến An', 'Quận Kiến An', 'Quận', '31'),
('308', 'Đồ Sơn', 'Quận Đồ Sơn', 'Quận', '31'),
('309', 'Dương Kinh', 'Quận Dương Kinh', 'Quận', '31'),

-- Cần Thơ
('916', 'Ninh Kiều', 'Quận Ninh Kiều', 'Quận', '92'),
('917', 'Ô Môn', 'Quận Ô Môn', 'Quận', '92'),
('918', 'Bình Thủy', 'Quận Bình Thủy', 'Quận', '92'),
('919', 'Cái Răng', 'Quận Cái Răng', 'Quận', '92'),
('923', 'Thốt Nốt', 'Quận Thốt Nốt', 'Quận', '92'),

-- Thừa Thiên Huế
('474', 'Huế', 'Thành phố Huế', 'Thành phố', '46'),
('476', 'Hương Thủy', 'Thị xã Hương Thủy', 'Thị xã', '46'),
('477', 'Hương Trà', 'Thị xã Hương Trà', 'Thị xã', '46'),

-- Khánh Hòa
('568', 'Nha Trang', 'Thành phố Nha Trang', 'Thành phố', '56'),
('569', 'Cam Ranh', 'Thành phố Cam Ranh', 'Thành phố', '56'),

-- Đồng Nai
('731', 'Biên Hòa', 'Thành phố Biên Hòa', 'Thành phố', '75'),
('732', 'Long Khánh', 'Thành phố Long Khánh', 'Thành phố', '75'),

-- Bình Dương
('718', 'Thủ Dầu Một', 'Thành phố Thủ Dầu Một', 'Thành phố', '74'),
('719', 'Thuận An', 'Thành phố Thuận An', 'Thành phố', '74'),
('720', 'Dĩ An', 'Thành phố Dĩ An', 'Thành phố', '74'),
('721', 'Tân Uyên', 'Thành phố Tân Uyên', 'Thành phố', '74'),

-- Bà Rịa - Vũng Tàu
('747', 'Vũng Tàu', 'Thành phố Vũng Tàu', 'Thành phố', '77'),
('748', 'Bà Rịa', 'Thành phố Bà Rịa', 'Thành phố', '77'),

-- Các tỉnh mới thêm - thành phố trung tâm tỉnh lỵ
('024', 'Hà Giang', 'Thành phố Hà Giang', 'Thành phố', '02'),
('040', 'Cao Bằng', 'Thành phố Cao Bằng', 'Thành phố', '04'),
('058', 'Bắc Kạn', 'Thành phố Bắc Kạn', 'Thành phố', '06'),
('070', 'Tuyên Quang', 'Thành phố Tuyên Quang', 'Thành phố', '08'),
('080', 'Lào Cai', 'Thành phố Lào Cai', 'Thành phố', '10'),
('094', 'Điện Biên Phủ', 'Thành phố Điện Biên Phủ', 'Thành phố', '11'),
('105', 'Lai Châu', 'Thành phố Lai Châu', 'Thành phố', '12'),
('116', 'Sơn La', 'Thành phố Sơn La', 'Thành phố', '14'),
('132', 'Yên Bái', 'Thành phố Yên Bái', 'Thành phố', '15'),
('148', 'Hòa Bình', 'Thành phố Hòa Bình', 'Thành phố', '17'),
('164', 'Thái Nguyên', 'Thành phố Thái Nguyên', 'Thành phố', '19'),
('178', 'Lạng Sơn', 'Thành phố Lạng Sơn', 'Thành phố', '20'),
('193', 'Hạ Long', 'Thành phố Hạ Long', 'Thành phố', '22'),
('194', 'Cẩm Phả', 'Thành phố Cẩm Phả', 'Thành phố', '22'),
('195', 'Uông Bí', 'Thành phố Uông Bí', 'Thành phố', '22'),
('196', 'Móng Cái', 'Thành phố Móng Cái', 'Thành phố', '22'),
('213', 'Bắc Giang', 'Thành phố Bắc Giang', 'Thành phố', '24'),
('227', 'Việt Trì', 'Thành phố Việt Trì', 'Thành phố', '25'),
('243', 'Vĩnh Yên', 'Thành phố Vĩnh Yên', 'Thành phố', '26'),
('256', 'Bắc Ninh', 'Thành phố Bắc Ninh', 'Thành phố', '27'),
('288', 'Hải Dương', 'Thành phố Hải Dương', 'Thành phố', '30'),
('323', 'Hưng Yên', 'Thành phố Hưng Yên', 'Thành phố', '33'),
('336', 'Thái Bình', 'Thành phố Thái Bình', 'Thành phố', '34'),
('347', 'Phủ Lý', 'Thành phố Phủ Lý', 'Thành phố', '35'),
('356', 'Nam Định', 'Thành phố Nam Định', 'Thành phố', '36'),
('369', 'Ninh Bình', 'Thành phố Ninh Bình', 'Thành phố', '37'),
('380', 'Thanh Hóa', 'Thành phố Thanh Hóa', 'Thành phố', '38'),
('381', 'Sầm Sơn', 'Thành phố Sầm Sơn', 'Thành phố', '38'),
('412', 'Vinh', 'Thành phố Vinh', 'Thành phố', '40'),
('413', 'Cửa Lò', 'Thị xã Cửa Lò', 'Thị xã', '40'),
('436', 'Hà Tĩnh', 'Thành phố Hà Tĩnh', 'Thành phố', '42'),
('450', 'Đồng Hới', 'Thành phố Đồng Hới', 'Thành phố', '44'),
('461', 'Đông Hà', 'Thành phố Đông Hà', 'Thành phố', '45'),
('502', 'Tam Kỳ', 'Thành phố Tam Kỳ', 'Thành phố', '49'),
('503', 'Hội An', 'Thành phố Hội An', 'Thành phố', '49'),
('522', 'Quảng Ngãi', 'Thành phố Quảng Ngãi', 'Thành phố', '51'),
('540', 'Quy Nhơn', 'Thành phố Quy Nhơn', 'Thành phố', '52'),
('555', 'Tuy Hòa', 'Thành phố Tuy Hòa', 'Thành phố', '54'),
('582', 'Phan Rang - Tháp Chàm', 'Thành phố Phan Rang - Tháp Chàm', 'Thành phố', '58'),
('593', 'Phan Thiết', 'Thành phố Phan Thiết', 'Thành phố', '60'),
('608', 'Kon Tum', 'Thành phố Kon Tum', 'Thành phố', '62'),
('622', 'Pleiku', 'Thành phố Pleiku', 'Thành phố', '64'),
('643', 'Buôn Ma Thuột', 'Thành phố Buôn Ma Thuột', 'Thành phố', '66'),
('660', 'Gia Nghĩa', 'Thành phố Gia Nghĩa', 'Thành phố', '67'),
('672', 'Đà Lạt', 'Thành phố Đà Lạt', 'Thành phố', '68'),
('673', 'Bảo Lộc', 'Thành phố Bảo Lộc', 'Thành phố', '68'),
('688', 'Đồng Xoài', 'Thành phố Đồng Xoài', 'Thành phố', '70'),
('703', 'Tây Ninh', 'Thành phố Tây Ninh', 'Thành phố', '72'),
('794', 'Tân An', 'Thành phố Tân An', 'Thành phố', '80'),
('815', 'Mỹ Tho', 'Thành phố Mỹ Tho', 'Thành phố', '82'),
('829', 'Bến Tre', 'Thành phố Bến Tre', 'Thành phố', '83'),
('842', 'Trà Vinh', 'Thành phố Trà Vinh', 'Thành phố', '84'),
('855', 'Vĩnh Long', 'Thành phố Vĩnh Long', 'Thành phố', '86'),
('866', 'Cao Lãnh', 'Thành phố Cao Lãnh', 'Thành phố', '87'),
('867', 'Sa Đéc', 'Thành phố Sa Đéc', 'Thành phố', '87'),
('883', 'Long Xuyên', 'Thành phố Long Xuyên', 'Thành phố', '89'),
('884', 'Châu Đốc', 'Thành phố Châu Đốc', 'Thành phố', '89'),
('899', 'Rạch Giá', 'Thành phố Rạch Giá', 'Thành phố', '91'),
('900', 'Phú Quốc', 'Thành phố Phú Quốc', 'Thành phố', '91'),
('930', 'Vị Thanh', 'Thành phố Vị Thanh', 'Thành phố', '93'),
('941', 'Sóc Trăng', 'Thành phố Sóc Trăng', 'Thành phố', '94'),
('954', 'Bạc Liêu', 'Thành phố Bạc Liêu', 'Thành phố', '95'),
('964', 'Cà Mau', 'Thành phố Cà Mau', 'Thành phố', '96');

-- Thêm wards cho các quận/huyện mới bổ sung
INSERT INTO wards (code, name, full_name, unit_type, district_code) VALUES
-- Bình Thạnh - thêm phường còn lại
('26842', 'Phường 26', 'Phường 26', 'Phường', '765'),
('26845', 'Phường 27', 'Phường 27', 'Phường', '765'),
('26848', 'Phường 28', 'Phường 28', 'Phường', '765'),

-- TP. Thủ Đức - thêm phường còn lại
('26886', 'Linh Chiểu', 'Phường Linh Chiểu', 'Phường', '769'),
('26889', 'Linh Trung', 'Phường Linh Trung', 'Phường', '769'),
('26892', 'Bình Chiểu', 'Phường Bình Chiểu', 'Phường', '769'),

-- Quận 1 - thêm phường còn lại
('26746', 'Phạm Ngũ Lão', 'Phường Phạm Ngũ Lão', 'Phường', '760'),
('26749', 'Cô Giang', 'Phường Cô Giang', 'Phường', '760'),

-- Quận 3
('27130', 'Võ Thị Sáu', 'Phường Võ Thị Sáu', 'Phường', '770'),
('27133', 'Phường 1', 'Phường 1', 'Phường', '770'),
('27136', 'Phường 2', 'Phường 2', 'Phường', '770'),
('27139', 'Phường 3', 'Phường 3', 'Phường', '770'),

-- Quận 7
('27448', 'Tân Phú', 'Phường Tân Phú', 'Phường', '778'),
('27451', 'Tân Phong', 'Phường Tân Phong', 'Phường', '778'),
('27454', 'Phú Mỹ', 'Phường Phú Mỹ', 'Phường', '778'),
('27457', 'Bình Thuận', 'Phường Bình Thuận', 'Phường', '778'),

-- Quận 10
('27178', 'Phường 12', 'Phường 12', 'Phường', '771'),
('27181', 'Phường 14', 'Phường 14', 'Phường', '771'),
('27184', 'Phường 15', 'Phường 15', 'Phường', '771'),

-- Phú Nhuận
('27040', 'Phường 1', 'Phường 1', 'Phường', '768'),
('27043', 'Phường 2', 'Phường 2', 'Phường', '768'),

-- Gò Vấp
('26770', 'Phường 1', 'Phường 1', 'Phường', '764'),
('26773', 'Phường 3', 'Phường 3', 'Phường', '764'),

-- Tân Bình
('26950', 'Phường 2', 'Phường 2', 'Phường', '766'),
('26953', 'Phường 4', 'Phường 4', 'Phường', '766'),

-- Tân Phú
('27250', 'Phường Tây Thạnh', 'Phường Tây Thạnh', 'Phường', '767'),
('27253', 'Phường Sơn Kỳ', 'Phường Sơn Kỳ', 'Phường', '767'),

-- Bình Tân
('27610', 'Bình Hưng Hòa', 'Phường Bình Hưng Hòa', 'Phường', '777'),
('27613', 'Bình Trị Đông', 'Phường Bình Trị Đông', 'Phường', '777'),

-- Quận 12
('26800', 'Thạnh Xuân', 'Phường Thạnh Xuân', 'Phường', '761'),
('26803', 'Thạnh Lộc', 'Phường Thạnh Lộc', 'Phường', '761'),

-- Quận 11
('27331', 'Phường 1', 'Phường 1', 'Phường', '772'),
('27334', 'Phường 2', 'Phường 2', 'Phường', '772'),

-- Quận 4
('27196', 'Phường 1', 'Phường 1', 'Phường', '773'),
('27199', 'Phường 2', 'Phường 2', 'Phường', '773'),

-- Quận 5
('27244', 'Phường 1', 'Phường 1', 'Phường', '774'),
('27247', 'Phường 2', 'Phường 2', 'Phường', '774'),

-- Quận 6
('27286', 'Phường 1', 'Phường 1', 'Phường', '775'),
('27289', 'Phường 2', 'Phường 2', 'Phường', '775'),

-- Quận 8
('27364', 'Phường 1', 'Phường 1', 'Phường', '776'),
('27367', 'Phường 2', 'Phường 2', 'Phường', '776'),

-- Ba Đình (002)
('00030', 'Điện Biên', 'Phường Điện Biên', 'Phường', '002'),
('00033', 'Đội Cấn', 'Phường Đội Cấn', 'Phường', '002'),
('00036', 'Ngọc Hà', 'Phường Ngọc Hà', 'Phường', '002'),

-- Đống Đa (006)
('00190', 'Cát Linh', 'Phường Cát Linh', 'Phường', '006'),
('00193', 'Láng Hạ', 'Phường Láng Hạ', 'Phường', '006'),
('00196', 'Thịnh Quang', 'Phường Thịnh Quang', 'Phường', '006'),

-- Hai Bà Trưng (007)
('00250', 'Bách Khoa', 'Phường Bách Khoa', 'Phường', '007'),
('00253', 'Minh Khai', 'Phường Minh Khai', 'Phường', '007'),

-- Hà Đông (016)
('00460', 'Mộ Lao', 'Phường Mộ Lao', 'Phường', '016'),
('00463', 'Văn Quán', 'Phường Văn Quán', 'Phường', '016'),

-- Nam Từ Liêm (019)
('00500', 'Mỹ Đình 1', 'Phường Mỹ Đình 1', 'Phường', '019'),
('00503', 'Mỹ Đình 2', 'Phường Mỹ Đình 2', 'Phường', '019'),

-- Bắc Từ Liêm (021)
('00520', 'Cổ Nhuế 1', 'Phường Cổ Nhuế 1', 'Phường', '021'),
('00523', 'Cổ Nhuế 2', 'Phường Cổ Nhuế 2', 'Phường', '021'),

-- Thanh Khê (491)
('20220', 'Thanh Khê Đông', 'Phường Thanh Khê Đông', 'Phường', '491'),
('20223', 'Thanh Khê Tây', 'Phường Thanh Khê Tây', 'Phường', '491'),

-- Ngũ Hành Sơn (493)
('20260', 'Mỹ An', 'Phường Mỹ An', 'Phường', '493'),
('20263', 'Khuê Mỹ', 'Phường Khuê Mỹ', 'Phường', '493'),

-- Liên Chiểu (494)
('20278', 'Hòa Khánh Bắc', 'Phường Hòa Khánh Bắc', 'Phường', '494'),
('20281', 'Hòa Khánh Nam', 'Phường Hòa Khánh Nam', 'Phường', '494'),

-- Cẩm Lệ (495)
('20293', 'Khuê Trung', 'Phường Khuê Trung', 'Phường', '495'),
('20296', 'Hòa Thọ Đông', 'Phường Hòa Thọ Đông', 'Phường', '495'),

-- Hồng Bàng - Hải Phòng (303)
('11350', 'Minh Khai', 'Phường Minh Khai', 'Phường', '303'),
('11353', 'Hoàng Văn Thụ', 'Phường Hoàng Văn Thụ', 'Phường', '303'),

-- Ngô Quyền - Hải Phòng (304)
('11400', 'Cầu Đất', 'Phường Cầu Đất', 'Phường', '304'),
('11403', 'Lạch Tray', 'Phường Lạch Tray', 'Phường', '304'),

-- Ninh Kiều - Cần Thơ (916)
('31150', 'Tân An', 'Phường Tân An', 'Phường', '916'),
('31153', 'An Cư', 'Phường An Cư', 'Phường', '916'),
('31156', 'Xuân Khánh', 'Phường Xuân Khánh', 'Phường', '916'),
('31159', 'An Hòa', 'Phường An Hòa', 'Phường', '916'),

-- Huế (474)
('19700', 'Vĩnh Ninh', 'Phường Vĩnh Ninh', 'Phường', '474'),
('19703', 'Phú Nhuận', 'Phường Phú Nhuận', 'Phường', '474'),
('19706', 'Phú Hội', 'Phường Phú Hội', 'Phường', '474'),

-- Nha Trang (568)
('22300', 'Lộc Thọ', 'Phường Lộc Thọ', 'Phường', '568'),
('22303', 'Tân Lập', 'Phường Tân Lập', 'Phường', '568'),
('22306', 'Phương Sài', 'Phường Phương Sài', 'Phường', '568'),

-- Biên Hòa (731)
('26000', 'Quyết Thắng', 'Phường Quyết Thắng', 'Phường', '731'),
('26003', 'Trung Dũng', 'Phường Trung Dũng', 'Phường', '731'),

-- Thủ Dầu Một (718)
('25700', 'Phú Cường', 'Phường Phú Cường', 'Phường', '718'),
('25703', 'Phú Hòa', 'Phường Phú Hòa', 'Phường', '718'),

-- Thuận An (719)
('25750', 'Lái Thiêu', 'Phường Lái Thiêu', 'Phường', '719'),
('25753', 'Bình Chuẩn', 'Phường Bình Chuẩn', 'Phường', '719'),

-- Dĩ An (720)
('25800', 'Dĩ An', 'Phường Dĩ An', 'Phường', '720'),
('25803', 'Tân Đông Hiệp', 'Phường Tân Đông Hiệp', 'Phường', '720'),

-- Vũng Tàu (747)
('26500', 'Phường 1', 'Phường 1', 'Phường', '747'),
('26503', 'Thắng Tam', 'Phường Thắng Tam', 'Phường', '747'),
('26506', 'Thắng Nhì', 'Phường Thắng Nhì', 'Phường', '747'),

-- Hạ Long (193)
('06700', 'Bạch Đằng', 'Phường Bạch Đằng', 'Phường', '193'),
('06703', 'Bãi Cháy', 'Phường Bãi Cháy', 'Phường', '193'),

-- Vinh (412)
('17500', 'Quang Trung', 'Phường Quang Trung', 'Phường', '412'),
('17503', 'Lê Mao', 'Phường Lê Mao', 'Phường', '412'),

-- Buôn Ma Thuột (643)
('24000', 'Tân An', 'Phường Tân An', 'Phường', '643'),
('24003', 'Thắng Lợi', 'Phường Thắng Lợi', 'Phường', '643'),

-- Đà Lạt (672)
('24800', 'Phường 1', 'Phường 1', 'Phường', '672'),
('24803', 'Phường 2', 'Phường 2', 'Phường', '672'),
('24806', 'Phường 3', 'Phường 3', 'Phường', '672'),

-- Quy Nhơn (540)
('22700', 'Trần Phú', 'Phường Trần Phú', 'Phường', '540'),
('22703', 'Ngô Mây', 'Phường Ngô Mây', 'Phường', '540'),

-- Phan Thiết (593)
('23300', 'Đức Nghĩa', 'Phường Đức Nghĩa', 'Phường', '593'),
('23303', 'Đức Thắng', 'Phường Đức Thắng', 'Phường', '593'),

-- Pleiku (622)
('23500', 'Hội Thương', 'Phường Hội Thương', 'Phường', '622'),
('23503', 'Yên Thế', 'Phường Yên Thế', 'Phường', '622'),

-- Kon Tum (608)
('23100', 'Quyết Thắng', 'Phường Quyết Thắng', 'Phường', '608'),
('23103', 'Trần Hưng Đạo', 'Phường Trần Hưng Đạo', 'Phường', '608'),

-- Long Xuyên (883)
('29500', 'Mỹ Bình', 'Phường Mỹ Bình', 'Phường', '883'),
('29503', 'Mỹ Long', 'Phường Mỹ Long', 'Phường', '883'),

-- Rạch Giá (899)
('30000', 'Vĩnh Thanh Vân', 'Phường Vĩnh Thanh Vân', 'Phường', '899'),
('30003', 'Vĩnh Thanh', 'Phường Vĩnh Thanh', 'Phường', '899'),

-- Phú Quốc (900)
('30050', 'Dương Đông', 'Phường Dương Đông', 'Phường', '900'),
('30053', 'An Thới', 'Phường An Thới', 'Phường', '900'),

-- Các phường mặc định cho các thành phố tỉnh lỵ còn lại
('01001', 'Phường Nguyễn Trãi', 'Phường Nguyễn Trãi', 'Phường', '024'),
('01002', 'Phường Sông Hiến', 'Phường Sông Hiến', 'Phường', '040'),
('01003', 'Phường Đức Xuân', 'Phường Đức Xuân', 'Phường', '058'),
('01004', 'Phường Tân Quang', 'Phường Tân Quang', 'Phường', '070'),
('01005', 'Phường Kim Tân', 'Phường Kim Tân', 'Phường', '080'),
('01006', 'Phường Mường Thanh', 'Phường Mường Thanh', 'Phường', '094'),
('01007', 'Phường Tân Phong', 'Phường Tân Phong', 'Phường', '105'),
('01008', 'Phường Quyết Thắng', 'Phường Quyết Thắng', 'Phường', '116'),
('01009', 'Phường Đồng Tâm', 'Phường Đồng Tâm', 'Phường', '132'),
('01010', 'Phường Phương Lâm', 'Phường Phương Lâm', 'Phường', '148'),
('01011', 'Phường Phan Đình Phùng', 'Phường Phan Đình Phùng', 'Phường', '164'),
('01012', 'Phường Vĩnh Trại', 'Phường Vĩnh Trại', 'Phường', '178'),
('01013', 'Phường Hoàng Văn Thụ', 'Phường Hoàng Văn Thụ', 'Phường', '213'),
('01014', 'Phường Tiên Cát', 'Phường Tiên Cát', 'Phường', '227'),
('01015', 'Phường Ngô Quyền', 'Phường Ngô Quyền', 'Phường', '243'),
('01016', 'Phường Tiền An', 'Phường Tiền An', 'Phường', '256'),
('01017', 'Phường Lê Thanh Nghị', 'Phường Lê Thanh Nghị', 'Phường', '288'),
('01018', 'Phường Hiến Nam', 'Phường Hiến Nam', 'Phường', '323'),
('01019', 'Phường Lê Hồng Phong', 'Phường Lê Hồng Phong', 'Phường', '336'),
('01020', 'Phường Minh Khai', 'Phường Minh Khai', 'Phường', '347'),
('01021', 'Phường Vị Hoàng', 'Phường Vị Hoàng', 'Phường', '356'),
('01022', 'Phường Vân Giang', 'Phường Vân Giang', 'Phường', '369'),
('01023', 'Phường Ba Đình', 'Phường Ba Đình', 'Phường', '380'),
('01024', 'Phường Sầm Sơn', 'Phường Sầm Sơn', 'Phường', '381'),
('01025', 'Phường Quán Bàu', 'Phường Quán Bàu', 'Phường', '412'),
('01026', 'Phường Nghi Hòa', 'Phường Nghi Hòa', 'Phường', '413'),
('01027', 'Phường Bắc Hà', 'Phường Bắc Hà', 'Phường', '436'),
('01028', 'Phường Đồng Mỹ', 'Phường Đồng Mỹ', 'Phường', '450'),
('01029', 'Phường 1', 'Phường 1', 'Phường', '461'),
('01030', 'Phường Phú Hội', 'Phường Phú Hội', 'Phường', '476'),
('01031', 'Phường Tứ Hạ', 'Phường Tứ Hạ', 'Phường', '477'),
('01032', 'Phường An Mỹ', 'Phường An Mỹ', 'Phường', '502'),
('01033', 'Phường Minh An', 'Phường Minh An', 'Phường', '503'),
('01034', 'Phường Trần Hưng Đạo', 'Phường Trần Hưng Đạo', 'Phường', '522'),
('01035', 'Phường 1', 'Phường 1', 'Phường', '555'),
('01036', 'Phường Kinh Dinh', 'Phường Kinh Dinh', 'Phường', '582'),
('01037', 'Phường Nghĩa Đức', 'Phường Nghĩa Đức', 'Phường', '660'),
('01038', 'Phường Bảo Lộc', 'Phường Bảo Lộc', 'Phường', '673'),
('01039', 'Phường Tân Phú', 'Phường Tân Phú', 'Phường', '688'),
('01040', 'Phường 1', 'Phường 1', 'Phường', '703'),
('01041', 'Phường Tân Uyên', 'Phường Tân Uyên', 'Phường', '721'),
('01042', 'Phường Long Khánh', 'Phường Long Khánh', 'Phường', '732'),
('01043', 'Phường Bà Rịa', 'Phường Bà Rịa', 'Phường', '748'),
('01044', 'Phường 1', 'Phường 1', 'Phường', '794'),
('01045', 'Phường 1', 'Phường 1', 'Phường', '815'),
('01046', 'Phường 1', 'Phường 1', 'Phường', '829'),
('01047', 'Phường 1', 'Phường 1', 'Phường', '842'),
('01048', 'Phường 1', 'Phường 1', 'Phường', '855'),
('01049', 'Phường 1', 'Phường 1', 'Phường', '866'),
('01050', 'Phường 1', 'Phường 1', 'Phường', '867'),
('01051', 'Phường 1', 'Phường 1', 'Phường', '884'),
('01052', 'Phường 1', 'Phường 1', 'Phường', '930'),
('01053', 'Phường 1', 'Phường 1', 'Phường', '941'),
('01054', 'Phường 1', 'Phường 1', 'Phường', '954'),
('01055', 'Phường 1', 'Phường 1', 'Phường', '964'),
-- Huyện ngoại thành HCM
('01056', 'Tân Thạnh Đông', 'Xã Tân Thạnh Đông', 'Xã', '783'),
('01057', 'Tân Hiệp', 'Xã Tân Hiệp', 'Xã', '784'),
('01058', 'Tân Kiên', 'Xã Tân Kiên', 'Xã', '785'),
('01059', 'Hiệp Phước', 'Xã Hiệp Phước', 'Xã', '786'),
('01060', 'Cần Thạnh', 'Thị trấn Cần Thạnh', 'Thị trấn', '787'),
-- Huyện ngoại thành Hà Nội
('01061', 'Thị trấn Tây Đằng', 'Thị trấn Tây Đằng', 'Thị trấn', '018'),
('01062', 'Thị trấn Chi Đông', 'Thị trấn Chi Đông', 'Thị trấn', '020'),
('01063', 'Xuân Canh', 'Xã Xuân Canh', 'Xã', '250'),
('01064', 'Đa Tốn', 'Xã Đa Tốn', 'Xã', '268'),
-- Sơn Tây
('01065', 'Lê Lợi', 'Phường Lê Lợi', 'Phường', '017'),
-- Huyện Hòa Vang - Đà Nẵng
('01066', 'Hòa Phước', 'Xã Hòa Phước', 'Xã', '497'),
-- Ô Môn, Bình Thủy, Cái Răng, Thốt Nốt - Cần Thơ
('01067', 'Phường Châu Văn Liêm', 'Phường Châu Văn Liêm', 'Phường', '917'),
('01068', 'Phường Bình Thủy', 'Phường Bình Thủy', 'Phường', '918'),
('01069', 'Phường Lê Bình', 'Phường Lê Bình', 'Phường', '919'),
('01070', 'Phường Thốt Nốt', 'Phường Thốt Nốt', 'Phường', '923'),
-- Cam Ranh - Khánh Hòa
('01073', 'Phường Cam Nghĩa', 'Phường Cam Nghĩa', 'Phường', '569');

