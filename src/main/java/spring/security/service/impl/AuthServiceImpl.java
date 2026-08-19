package spring.security.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.request.LoginRequest;
import spring.security.dto.request.RegisterRequest;
import spring.security.dto.response.TokenResponse;
import spring.security.dto.response.UserResponse;
import spring.security.entity.Roles;
import spring.security.entity.Users;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.UserMapper;
import spring.security.repository.RoleRepository;
import spring.security.repository.UserRepository;
import spring.security.security.jwt.JwtUtils;
import spring.security.security.user.CustomUserDetails;
import spring.security.service.AuthService;
import spring.security.service.IssuedRefreshToken;
import spring.security.service.RefreshTokenService;
import spring.security.service.RotatedRefreshToken;
import spring.security.utils.CookieUtils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtils cookieUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           UserMapper userMapper,
                           RefreshTokenService refreshTokenService,
                           CookieUtils cookieUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {
        String username = normalizeUsername(request.getUsername());
        log.info("[LOGIN] Authentication attempt");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtils.generateToken(userDetails.getUsername());
        IssuedRefreshToken refreshToken = refreshTokenService.issueForUser(userDetails.getUser().getId());
        addRefreshCookie(response, refreshToken.value());
        log.info("[LOGIN] Authentication succeeded");
        return new TokenResponse(accessToken);
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        String username = normalizeUsername(request.getUsername());
        if (userRepository.existsByUsernameAndDeletedFalse(username)) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        Roles userRole = roleRepository.findByNameAndDeletedFalse("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        Users user = Users.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        log.info("[REGISTER] User registered successfully");
        return userMapper.toUserResponse(user);
    }

    @Override
    public TokenResponse getNewRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String oldToken = refreshTokenService.getRefreshTokenFromCookie(request);
        RotatedRefreshToken rotated = refreshTokenService.rotate(oldToken);
        String accessToken = jwtUtils.generateToken(rotated.user().getUsername());
        addRefreshCookie(response, rotated.value());
        return new TokenResponse(accessToken);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.revoke(refreshTokenService.getRefreshTokenFromCookie(request));
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.clearRefreshTokenCookie().toString());
        log.info("[LOGOUT] Refresh token revoked");
    }

    private void addRefreshCookie(HttpServletResponse response, String value) {
        ResponseCookie cookie = cookieUtils.buildRefreshTokenCookie(value);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
