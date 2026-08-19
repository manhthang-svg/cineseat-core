package spring.security.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.config.properties.JwtProperties;
import spring.security.entity.RefreshToken;
import spring.security.entity.Users;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.repository.RefreshTokenRepository;
import spring.security.repository.UserRepository;
import spring.security.service.IssuedRefreshToken;
import spring.security.service.RotatedRefreshToken;
import spring.security.utils.TokenUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {
    private static final Instant NOW = Instant.parse("2026-07-18T00:00:00Z");

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private HttpServletRequest request;

    private RefreshTokenServiceImpl service;
    private Users activeUser;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(
                "unused", 900_000, 604_800_000,
                "ticketing-system", "ticketing-api", 30);
        service = new RefreshTokenServiceImpl(
                userRepository,
                refreshTokenRepository,
                properties,
                Clock.fixed(NOW, ZoneOffset.UTC));
        activeUser = Users.builder()
                .username("user@example.com")
                .password("encoded")
                .build();
        activeUser.setId(1L);
        activeUser.setDeleted(false);
    }

    @Test
    void issueStoresOnlyDigestAndCreatesIndependentFamily() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));

        IssuedRefreshToken issued = service.issueForUser(1L);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken stored = captor.getValue();
        assertThat(stored.getTokenHash()).isEqualTo(TokenUtils.sha256(issued.value()));
        assertThat(stored.getTokenHash()).doesNotContain(issued.value());
        assertThat(stored.getFamilyId()).isNotBlank();
        assertThat(stored.getExpiryDate()).isEqualTo(NOW.plusMillis(604_800_000));
    }

    @Test
    void rotateRevokesOldTokenAndKeepsFamily() {
        String oldRaw = "old-token";
        RefreshToken current = activeToken(oldRaw);
        when(refreshTokenRepository.findByTokenHashForUpdate(TokenUtils.sha256(oldRaw)))
                .thenReturn(Optional.of(current));

        RotatedRefreshToken rotated = service.rotate(oldRaw);

        assertThat(current.getRevokedAt()).isEqualTo(NOW);
        assertThat(current.getReplacedByTokenHash()).isEqualTo(TokenUtils.sha256(rotated.value()));
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getFamilyId()).isEqualTo("family-1");
        assertThat(captor.getValue().getUsers()).isSameAs(activeUser);
    }

    @Test
    void replayRevokesEntireFamily() {
        RefreshToken current = activeToken("replayed-token");
        current.setRevokedAt(NOW.minusSeconds(1));
        when(refreshTokenRepository.findByTokenHashForUpdate(any()))
                .thenReturn(Optional.of(current));

        assertThatThrownBy(() -> service.rotate("replayed-token"))
                .isInstanceOfSatisfying(AppException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(ErrorCode.REFRESH_TOKEN_REUSED));
        verify(refreshTokenRepository).revokeFamily("family-1", NOW);
    }

    @Test
    void expiredTokenIsRevoked() {
        RefreshToken current = activeToken("expired-token");
        current.setExpiryDate(NOW);
        when(refreshTokenRepository.findByTokenHashForUpdate(any()))
                .thenReturn(Optional.of(current));

        assertThatThrownBy(() -> service.rotate("expired-token"))
                .isInstanceOfSatisfying(AppException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(ErrorCode.REFRESH_TOKEN_EXPIRED));
        assertThat(current.getRevokedAt()).isEqualTo(NOW);
    }

    private RefreshToken activeToken(String rawToken) {
        return RefreshToken.builder()
                .tokenHash(TokenUtils.sha256(rawToken))
                .familyId("family-1")
                .users(activeUser)
                .expiryDate(NOW.plusSeconds(3600))
                .build();
    }
}
