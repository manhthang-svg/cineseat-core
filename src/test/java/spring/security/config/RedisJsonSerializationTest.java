package spring.security.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import spring.security.dto.response.MovieResponse;
import spring.security.dto.response.PageResponse;
import spring.security.enums.MovieStatus;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RedisJsonSerializationTest {

    @Test
    void moviePageResponse_RoundTripsWithItsConcreteType() {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("spring.security.dto.response.")
                .allowIfSubType("java.util.")
                .build();
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .enableDefaultTyping(typeValidator)
                .build();
        MovieResponse movie = MovieResponse.builder()
                .id(1L)
                .title("Interstellar")
                .durationMinutes(169)
                .releaseDate(LocalDate.of(2014, 11, 7))
                .status(MovieStatus.ACTIVE)
                .build();
        PageResponse<MovieResponse> source = new PageResponse<>(
                List.of(movie), 0, 12, 1, 1, true, true
        );

        Object restored = serializer.deserialize(serializer.serialize(source));

        assertThat(restored).isInstanceOf(PageResponse.class);
        PageResponse<?> restoredPage = (PageResponse<?>) restored;
        assertThat(restoredPage.content()).singleElement().isInstanceOf(MovieResponse.class);
        MovieResponse restoredMovie = (MovieResponse) restoredPage.content().getFirst();
        assertThat(restoredMovie.getTitle()).isEqualTo("Interstellar");
        assertThat(restoredMovie.getReleaseDate()).isEqualTo(LocalDate.of(2014, 11, 7));
    }
}
