package spring.security.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_001", "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_002", "Email đã tồn tại", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND("USER_003", "Role không tồn tại", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("AUTH_001", "Chưa đăng nhập hoặc token không hợp lệ", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH_003", "Không có quyền thực hiện hành động này", HttpStatus.FORBIDDEN),
    REFRESH_TOKEN_EXPIRED("AUTH_004", "Refresh token đã hết hạn", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("AUTH_005", "Không tìm thấy refresh token", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("AUTH_006", "Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_REUSED("AUTH_007", "Phát hiện refresh token đã được sử dụng; phiên đăng nhập đã bị thu hồi", HttpStatus.UNAUTHORIZED),
    MOVIE_NOT_FOUND("MOVIE_001", "Phim không tồn tại", HttpStatus.NOT_FOUND),
    MOVIE_ALREADY_EXISTS("MOVIE_002", "Tên phim đã tồn tại", HttpStatus.CONFLICT),
    MOVIE_HAS_SHOWTIMES("MOVIE_003", "Không thể xóa phim đã có suất chiếu", HttpStatus.CONFLICT),
    CINEMA_NOT_FOUND("CINEMA_001", "Rạp không tồn tại", HttpStatus.NOT_FOUND),
    CINEMA_ALREADY_EXISTS("CINEMA_002", "Tên rạp đã tồn tại", HttpStatus.CONFLICT),
    CINEMA_HAS_ROOMS("CINEMA_003", "Không thể xóa rạp đang có phòng chiếu", HttpStatus.CONFLICT),
    CINEMA_INACTIVE("CINEMA_004", "Rạp đang ở trạng thái không hoạt động", HttpStatus.BAD_REQUEST),
    ROOM_NOT_FOUND("ROOM_001", "Phòng chiếu không tồn tại", HttpStatus.NOT_FOUND),
    ROOM_ALREADY_EXISTS("ROOM_002", "Tên phòng chiếu đã tồn tại trong rạp này", HttpStatus.CONFLICT),
    ROOM_HAS_SEATS("ROOM_003", "Không thể xóa phòng chiếu đang có ghế", HttpStatus.CONFLICT),
    ROOM_HAS_SHOWTIMES("ROOM_004", "Không thể xóa phòng chiếu đang có suất chiếu", HttpStatus.CONFLICT),
    SEAT_NOT_FOUND("SEAT_001", "Ghế không tồn tại", HttpStatus.NOT_FOUND),
    SEAT_ALREADY_EXISTS("SEAT_002", "Vị trí ghế đã tồn tại trong phòng chiếu này", HttpStatus.CONFLICT),
    SEAT_HAS_BOOKINGS("SEAT_003", "Không thể xóa ghế đã có lịch sử đặt vé", HttpStatus.CONFLICT),
    UNCATEGORIZED_EXCEPTION("SYS_999", "Lỗi hệ thống nội bộ, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
