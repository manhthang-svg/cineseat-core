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
