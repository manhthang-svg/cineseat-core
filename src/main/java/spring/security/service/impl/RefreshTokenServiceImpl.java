package spring.security.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.config.properties.JwtProperties;
import spring.security.entity.RefreshToken;
import spring.security.entity.Users;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.repository.RefreshTokenRepository;
import spring.security.repository.UserRepository;
import spring.security.service.IssuedRefreshToken;
import spring.security.service.RefreshTokenService;
import spring.security.service.RotatedRefreshToken;
import spring.security.utils.CookieUtils;
import spring.security.utils.TokenUtils;

import java.time.Clock;
import java.time.Instant;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final Clock clock;

    public RefreshTokenServiceImpl(UserRepository userRepository,
                                   RefreshTokenRepository refreshTokenRepository,
                                   JwtProperties jwtProperties,
                                   Clock clock) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProperties = jwtProperties;
        this.clock = clock;
    }

    @Override
    @Transactional
    public IssuedRefreshToken issueForUser(Long userId) {
        Users user = userRepository.findById(userId)
                .filter(this::isAccountActive)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String rawToken = TokenUtils.newOpaqueToken();
        Instant now = clock.instant();
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(TokenUtils.sha256(rawToken))
                .familyId(java.util.UUID.randomUUID().toString())
                .users(user)
                .expiryDate(now.plusMillis(jwtProperties.refreshExpirationMs()))
                .build();
        refreshTokenRepository.save(refreshToken);
        return new IssuedRefreshToken(rawToken);
    }

    @Override
    @Transactional(noRollbackFor = AppException.class)
    public RotatedRefreshToken rotate(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        RefreshToken current = refreshTokenRepository
                .findByTokenHashForUpdate(TokenUtils.sha256(rawToken))
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        Instant now = clock.instant();
        if (current.isRevoked()) {
            refreshTokenRepository.revokeFamily(current.getFamilyId(), now);
            throw new AppException(ErrorCode.REFRESH_TOKEN_REUSED);
        }
        if (current.isExpired(now)) {
            current.setRevokedAt(now);
            refreshTokenRepository.save(current);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        if (!isAccountActive(current.getUsers())) {
            refreshTokenRepository.revokeFamily(current.getFamilyId(), now);
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String replacementRawToken = TokenUtils.newOpaqueToken();
        String replacementHash = TokenUtils.sha256(replacementRawToken);
        current.setRevokedAt(now);
        current.setReplacedByTokenHash(replacementHash);
        refreshTokenRepository.save(current);

        RefreshToken replacement = RefreshToken.builder()
                .tokenHash(replacementHash)
                .familyId(current.getFamilyId())
                .users(current.getUsers())
                .expiryDate(now.plusMillis(jwtProperties.refreshExpirationMs()))
                .build();
        refreshTokenRepository.saveAndFlush(replacement);
        return new RotatedRefreshToken(replacementRawToken, current.getUsers());
    }

    @Override
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (CookieUtils.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        refreshTokenRepository.findByTokenHashForUpdate(TokenUtils.sha256(rawToken))
                .filter(token -> !token.isRevoked())
                .ifPresent(token -> token.setRevokedAt(clock.instant()));
    }

    private boolean isAccountActive(Users user) {
        return !Boolean.TRUE.equals(user.getDeleted())
                && Boolean.TRUE.equals(user.getEnabled())
                && !Boolean.TRUE.equals(user.getAccountLocked())
                && !Boolean.TRUE.equals(user.getAccountExpired());
    }
}
