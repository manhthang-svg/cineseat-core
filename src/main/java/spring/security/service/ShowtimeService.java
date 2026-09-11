package spring.security.service;

import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.dto.response.ShowtimeViewResponse;

import java.time.Instant;
import java.util.List;

public interface ShowtimeService {
    CreateShowtimeResponse createShowtime(CreateShowtimeRequest request);

    List<ShowtimeViewResponse> getMovieShowtimes(Long movieId, Instant from, Instant to);
}
