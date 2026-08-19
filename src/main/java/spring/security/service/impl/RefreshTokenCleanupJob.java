package spring.security.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import spring.security.repository.RefreshTokenRepository;

import java.time.Clock;

@Component
public class RefreshTokenCleanupJob {
    private final RefreshTokenRepository repository;
    private final Clock clock;

    public RefreshTokenCleanupJob(RefreshTokenRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Scheduled(cron = "${security.refresh-token.cleanup-cron:0 0 3 * * *}", zone = "UTC")
    @Transactional
    public void removeExpiredTokens() {
        repository.deleteExpired(clock.instant());
    }
}
