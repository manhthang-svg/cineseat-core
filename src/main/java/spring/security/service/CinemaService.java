package spring.security.service;

import spring.security.dto.request.CreateCinemaRequest;
import spring.security.dto.request.UpdateCinemaRequest;
import spring.security.dto.response.CinemaResponse;

import java.util.List;

public interface CinemaService {
    List<CinemaResponse> getActiveCinemas();

    CinemaResponse getCinemaById(Long id);

    CinemaResponse createCinema(CreateCinemaRequest request);

    CinemaResponse updateCinema(Long id, UpdateCinemaRequest request);

    void deleteCinema(Long id);
}
