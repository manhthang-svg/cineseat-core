package spring.security.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import spring.security.config.properties.CookieProperties;
import spring.security.config.properties.JwtProperties;

import java.time.Duration;

@Component
public class CookieUtils {
    private final CookieProperties properties;
    private final JwtProperties jwtProperties;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth";
    public CookieUtils(CookieProperties properties, JwtProperties jwtProperties) {
        this.properties = properties;
        this.jwtProperties = jwtProperties;
    }

    public ResponseCookie buildRefreshTokenCookie(String value) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(properties.secure())
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .maxAge(Duration.ofMillis(jwtProperties.refreshExpirationMs()))
                .sameSite(properties.sameSite())
                .build();
    }

    public ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(properties.secure())
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .maxAge(0)
                .sameSite(properties.sameSite())
                .build();
    }
}
