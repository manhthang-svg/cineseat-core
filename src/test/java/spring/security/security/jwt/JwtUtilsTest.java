package spring.security.security.jwt;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import spring.security.config.properties.JwtProperties;
import spring.security.entity.Users;
import spring.security.security.user.CustomUserDetails;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilsTest {
    private static final Instant NOW = Instant.parse("2026-07-18T00:00:00Z");
    private static final String KEY = Base64.getEncoder().encodeToString(
            "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8));
    private static final JwtProperties PROPERTIES =
            new JwtProperties(KEY, 900_000, 604_800_000, "ticketing-system", "ticketing-api", 30);

    @Test
    void generatesAndValidatesTokenForActiveUser() {
        JwtUtils jwtUtils = jwtUtilsAt(NOW);
        CustomUserDetails user = activeUser();
        String token = jwtUtils.generateToken(user.getUsername());

        assertThat(jwtUtils.extractUsername(token)).isEqualTo(user.getUsername());
        assertThat(jwtUtils.isTokenValid(token, user)).isTrue();
    }

    @Test
    void rejectsTokenForDisabledUser() {
        JwtUtils jwtUtils = jwtUtilsAt(NOW);
        CustomUserDetails user = activeUser();
        String token = jwtUtils.generateToken(user.getUsername());
        user.getUser().setEnabled(false);

        assertThat(jwtUtils.isTokenValid(token, user)).isFalse();
    }

    @Test
    void rejectsTamperedToken() {
        JwtUtils jwtUtils = jwtUtilsAt(NOW);
        String token = jwtUtils.generateToken("user@example.com");
        String tampered = token.substring(0, token.length() - 6) + "abcdef";

        assertThatThrownBy(() -> jwtUtils.extractUsername(tampered))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void failsFastForWeakSigningKey() {
        String weakKey = Base64.getEncoder().encodeToString("weak".getBytes(StandardCharsets.UTF_8));
        JwtProperties weakProperties =
                new JwtProperties(weakKey, 900_000, 604_800_000, "issuer", "audience", 30);

        assertThatThrownBy(() -> new JwtUtils(weakProperties, Clock.systemUTC()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("256 bits");
    }

    private JwtUtils jwtUtilsAt(Instant instant) {
        return new JwtUtils(PROPERTIES, Clock.fixed(instant, ZoneOffset.UTC));
    }

    private CustomUserDetails activeUser() {
        Users user = Users.builder()
                .username("user@example.com")
                .password("encoded")
                .build();
        user.setDeleted(false);
        return new CustomUserDetails(user);
    }
}
