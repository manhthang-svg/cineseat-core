package spring.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfigurationSource;
import spring.security.config.properties.CookieProperties;
import spring.security.security.RestAccessDeniedHandler;
import spring.security.security.RestAuthenticationEntryPoint;
import spring.security.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;
    private final CookieProperties cookieProperties;

    @Value("${springdoc.api-docs.enabled:true}")
    private boolean apiDocsEnabled;

    @Value("${springdoc.swagger-ui.enabled:true}")
    private boolean swaggerUiEnabled;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh-token",
            "/api/auth/csrf",
            "/api/movies/**",
            "/api/cinemas/**",
            "/api/rooms/**",
            "/actuator/health/**"
    };

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui/**", "/swagger-ui.html", "/webjars/**",
            "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfRepository.setCookiePath("/");
        csrfRepository.setCookieCustomizer(cookie -> cookie
                .secure(cookieProperties.secure())
                .sameSite(cookieProperties.sameSite()));

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepository)
                        .ignoringRequestMatchers("/api/auth/login", "/api/auth/register"))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll();
                    if (apiDocsEnabled || swaggerUiEnabled) {
                        auth.requestMatchers(SWAGGER_ENDPOINTS).permitAll();
                    }
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
