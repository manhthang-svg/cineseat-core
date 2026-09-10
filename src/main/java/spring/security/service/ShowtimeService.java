package spring.security.service;

import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;

public interface ShowtimeService {
    CreateShowtimeResponse createShowtime(CreateShowtimeRequest request);
}
