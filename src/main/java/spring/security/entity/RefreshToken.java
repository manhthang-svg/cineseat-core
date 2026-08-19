package spring.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "refresh_token", indexes = {
        @Index(name = "idx_refresh_token_family_id", columnList = "family_id"),
        @Index(name = "idx_refresh_token_user_id", columnList = "user_id")
})
public class RefreshToken extends AbstractEntity{
    @Column(name = "token_hash", nullable = false, unique = true, length = 64, updatable = false)
    private String tokenHash;

    @Column(name = "family_id", nullable = false, length = 36, updatable = false)
    private String familyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Users users;

    @Column(nullable = false)
    private Instant expiryDate;

    private Instant revokedAt;

    @Column(length = 64)
    private String replacedByTokenHash;

    public boolean isExpired(Instant now) {
        return !expiryDate.isAfter(now);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }
}
