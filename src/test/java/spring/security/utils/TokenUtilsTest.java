package spring.security.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenUtilsTest {
    @Test
    void generatesHighEntropyUrlSafeTokens() {
        String first = TokenUtils.newOpaqueToken();
        String second = TokenUtils.newOpaqueToken();

        assertThat(first).hasSize(43).matches("^[A-Za-z0-9_-]+$");
        assertThat(second).isNotEqualTo(first);
    }

    @Test
    void hashesTokensDeterministicallyWithoutRetainingRawValue() {
        String digest = TokenUtils.sha256("refresh-token");

        assertThat(digest).hasSize(64);
        assertThat(digest).isEqualTo(TokenUtils.sha256("refresh-token"));
        assertThat(digest).doesNotContain("refresh-token");
    }
}
