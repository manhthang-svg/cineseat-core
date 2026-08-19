package spring.security.service;

import jakarta.servlet.http.HttpServletRequest;

public interface RefreshTokenService {
    IssuedRefreshToken issueForUser(Long userId);

    RotatedRefreshToken rotate(String rawToken);

    String getRefreshTokenFromCookie(HttpServletRequest request);

    void revoke(String rawToken);
}
