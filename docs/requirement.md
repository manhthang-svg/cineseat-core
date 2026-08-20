# Cinema Booking System — Requirements

## 1. Overview

Cinema Booking System là hệ thống cho phép khách hàng tìm kiếm phim,
xem rạp và suất chiếu, chọn ghế, đặt vé và thanh toán trực tuyến.

Admin có thể quản lý phim, rạp, phòng chiếu, ghế, suất chiếu,
booking và người dùng.

Payment Gateway là hệ thống bên ngoài chịu trách nhiệm xử lý thanh toán.

---

# 2. Actors

## 2.1 Customer

Khách hàng sử dụng hệ thống để mua vé và quản lý booking.

## 2.2 Admin

Quản trị viên quản lý dữ liệu và hoạt động của hệ thống.

## 2.3 Payment Gateway

External system xử lý thanh toán và trả kết quả thanh toán
về Cinema Booking System.

---

# 3. Functional Requirements

## 3.1 Customer

### FR-CUS-01 — Register

Customer có thể tạo tài khoản bằng email, password và
các thông tin cần thiết.

### FR-CUS-02 — Login

Customer có thể đăng nhập bằng email và password.

### FR-CUS-03 — Browse Movies

Customer có thể xem danh sách các phim đang được cung cấp.

### FR-CUS-04 — View Movie Detail

Customer có thể xem thông tin chi tiết của một phim.

### FR-CUS-05 — Select Cinema

Customer có thể xem và lựa chọn rạp chiếu.

### FR-CUS-06 — View Showtimes

Customer có thể xem các suất chiếu của phim tại một rạp.

### FR-CUS-07 — Select Seats

Customer có thể xem sơ đồ ghế và lựa chọn các ghế còn trống
cho một suất chiếu.

### FR-CUS-08 — Create Booking

Customer có thể tạo booking cho các ghế đã chọn.

### FR-CUS-09 — Pay

Customer có thể thanh toán booking thông qua Payment Gateway.

### FR-CUS-10 — Cancel Booking

Customer có thể yêu cầu hủy booking

### FR-CUS-11 — View Booking History

Customer có thể xem danh sách các booking của mình.

### FR-CUS-12 — View Booking Detail

Customer có thể xem thông tin chi tiết của một booking.

---

# 4. Admin Functional Requirements

## 4.1 Movie Management

Admin có thể:

- Create movie
- Update movie
- View movie
- Deactivate movie

## 4.2 Cinema Management

Admin có thể:

- Create cinema
- Update cinema
- View cinema
- Deactivate cinema

## 4.3 Screening Room Management

Admin có thể:

- Create screening room
- Update screening room
- View screening room
- Deactivate screening room

## 4.4 Seat Management

Admin có thể:

- Create seats
- Update seats
- View seats
- Deactivate seats

## 4.5 Showtime Management

Admin có thể:

- Create showtime
- Update showtime
- View showtime
- Cancel/deactivate showtime

## 4.6 Booking Management

Admin có thể:

- View bookings
- View booking detail
- Search bookings
- View booking status

## 4.7 User Management

Admin có thể:

- View users
- View user detail
- Activate/deactivate user

---

# 5. Business Rules

## 5.1 User

### BR-USER-01

Email của mỗi account phải là duy nhất.

### BR-USER-02

Không thể đăng ký account bằng email đã tồn tại.

### BR-USER-03

Password phải đáp ứng password policy của hệ thống.

### BR-USER-04

Phone number không bắt buộc.

### BR-USER-05

Nếu Customer cung cấp phone number,
phone number phải duy nhất.

### BR-USER-06

Customer có thể login ngay sau khi đăng ký thành công.

### BR-USER-07

Login request phải được rate limit để hạn chế brute-force.

### BR-USER-08

Login thất bại không tự động khóa account.

### BR-USER-09

Customer phải cung cấp password hiện tại trước khi
thay đổi password.

### BR-USER-10

Customer có thể reset password thông qua email.

---

# 5.2 Movie

### BR-MOVIE-01

Movie duration phải nằm trong khoảng 30–300 phút.

### BR-MOVIE-02

Movie title phải là duy nhất trong hệ thống.

### BR-MOVIE-03

Movie không được hiển thị cho Customer nếu ở trạng thái INACTIVE.

### BR-MOVIE-04 🔴

Movie đã có hoặc booking không được hard delete.


# 5.3 Cinema

### BR-CINEMA-01

Mỗi cinema phải có tên xác định.

### BR-CINEMA-02

Cinema phải có ít nhất một screening room để có thể
phát hành showtime.

### BR-CINEMA-03

Cinema INACTIVE không được sử dụng để tạo showtime mới.

---

# 5.4 Screening Room

### BR-ROOM-01

Một screening room thuộc về đúng một cinema.

### BR-ROOM-02

Tên room phải unique trong cùng một cinema.

### BR-ROOM-03

Một room phải có ít nhất một seat trước khi có thể
tạo showtime.

### BR-ROOM-04

Room đang được sử dụng bởi showtime không được hard delete.

---

# 5.5 Seat

### BR-SEAT-01

Mỗi seat thuộc về đúng một screening room.

### BR-SEAT-02

Seat identifier phải unique trong cùng một screening room.

Ví dụ:

A1, A2, A3...

### BR-SEAT-03

Một seat không thể được bán cho hai Customer
trong cùng một showtime.

### BR-SEAT-04 

Seat được chọn trong quá trình booking có thể được
tạm thời giữ cho Customer.

### BR-SEAT-05 

Thời gian giữ seat là 5 phút 

---

# 5.6 Showtime

### BR-SHOWTIME-01

Một showtime phải thuộc về đúng một movie.

### BR-SHOWTIME-02

Một showtime phải diễn ra tại đúng một screening room.

### BR-SHOWTIME-03

Showtime phải có thời gian bắt đầu.

### BR-SHOWTIME-04

Hai showtime trong cùng một room không được
overlap về thời gian.

### BR-SHOWTIME-05

Showtime không thể được tạo nếu movie hoặc room
đang INACTIVE.

### BR-SHOWTIME-06 

Showtime đã có booking không được hard delete.

---

# 5.7 Booking

### BR-BOOKING-01

Booking phải thuộc về một Customer.

### BR-BOOKING-02

Booking phải thuộc về một showtime.

### BR-BOOKING-03

Booking phải chứa ít nhất một seat.

### BR-BOOKING-04

Một Customer không thể book một seat đã được
bán cho Customer khác trong cùng showtime.

### BR-BOOKING-05 

Booking chưa thanh toán có thời hạn là 10 phút.

### BR-BOOKING-06 

Booking hết hạn phải giải phóng các seat đang được giữ.

### BR-BOOKING-07

Booking đã thanh toán không thể được sử dụng lại
cho một transaction khác.

---

# 5.8 Payment

### BR-PAYMENT-01

Một booking phải được tạo trước khi thực hiện payment.

### BR-PAYMENT-02

Payment phải được liên kết với một booking.

### BR-PAYMENT-03

Payment thành công → booking được xác nhận.

### BR-PAYMENT-04

Payment thất bại → booking không được xác nhận.

### BR-PAYMENT-05

Một payment transaction không được xử lý thành
nhiều lần dẫn đến việc charge Customer nhiều lần.

### BR-PAYMENT-06 

Hệ thống phải xử lý trường hợp Payment Gateway
gửi callback nhiều lần.

---

# 5.9 Cancellation & Refund



### BR-CANCEL-05

Booking đã cancelled không thể được cancel lần nữa.

---

# 6. Booking Flow

Basic booking flow:

Customer
    ↓
Browse movies
    ↓
Select movie
    ↓
Select cinema
    ↓
Select showtime
    ↓
View available seats
    ↓
Select seats
    ↓
Create booking
    ↓
Hold selected seats
    ↓
Payment
    ↓
Payment Gateway
    ↓
Payment result
    ↓
Success / Failure