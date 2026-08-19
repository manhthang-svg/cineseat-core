package spring.security.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import spring.security.config.properties.CookieProperties;
import spring.security.config.properties.JwtProperties;

import static org.assertj.core.api.Assertions.assertThat;

class CookieUtilsTest {
    private final CookieUtils cookieUtils =
            new CookieUtils(
                    new CookieProperties(true, "Strict"),
                    new JwtProperties("unused", 900_000, 604_800_000,
                            "issuer", "audience", 30));

    @Test
    void createsHardenedRefreshCookie() {
        ResponseCookie cookie = cookieUtils.buildRefreshTokenCookie("secret");

        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecure()).isTrue();
        assertThat(cookie.getSameSite()).isEqualTo("Strict");
        assertThat(cookie.getPath()).isEqualTo("/api/auth");
        assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(604800);
    }

    @Test
    void clearCookieUsesSameScopeAndZeroMaxAge() {
        ResponseCookie cookie = cookieUtils.clearRefreshTokenCookie();

        assertThat(cookie.getPath()).isEqualTo("/api/auth");
        assertThat(cookie.getMaxAge().isZero()).isTrue();
        assertThat(cookie.isSecure()).isTrue();
    }
}
