package spring.security.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        @NotBlank String secretKey,
        @Positive long expiration,
        @Positive long refreshExpirationMs,
        @NotBlank String issuer,
        @NotBlank String audience,
        @PositiveOrZero long clockSkewSeconds
) {
}
