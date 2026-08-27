package spring.security.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.security.config.RequestCorrelationFilter;
import spring.security.dto.response.ErrorResponse;
import spring.security.enums.ErrorCode;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
            AppException exception, HttpServletRequest request) {
        ErrorCode code = exception.getErrorCode();
        log.warn("Business request rejected: code={}", code.getCode());
        return ResponseEntity.status(code.getHttpStatus())
                .body(error(code.getCode(), code.getMessage(), null, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> details = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> details.putIfAbsent(
                        fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.badRequest().body(
                error("VALIDATION_ERROR", "Request validation failed", details, request));
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            HandlerMethodValidationException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidRequestParameter(
            Exception exception, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                error("INVALID_PARAMETER", "Request parameter is invalid", null, request));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error("AUTH_002", "Email hoặc mật khẩu không chính xác", null, request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error(ErrorCode.ACCESS_DENIED.getCode(),
                        ErrorCode.ACCESS_DENIED.getMessage(), null, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            DataIntegrityViolationException exception, HttpServletRequest request) {
        log.warn("Database constraint rejected request");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("DATA_CONFLICT", "Dữ liệu đã tồn tại hoặc đang được tham chiếu", null, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncategorized(
            Exception exception, HttpServletRequest request) {
        log.error("Unhandled system error", exception);
        ErrorCode code = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity.status(code.getHttpStatus())
                .body(error(code.getCode(), code.getMessage(), null, request));
    }

    private ErrorResponse error(String code,
                                String message,
                                Map<String, String> details,
                                HttpServletRequest request) {
        Object requestId = request.getAttribute(RequestCorrelationFilter.ATTRIBUTE_NAME);
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .details(details)
                .path(request.getRequestURI())
                .requestId(requestId == null ? null : requestId.toString())
                .timestamp(Instant.now())
                .build();
    }
}
