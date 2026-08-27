package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.request.CreateCinemaRequest;
import spring.security.dto.request.UpdateCinemaRequest;
import spring.security.dto.response.CinemaResponse;
import spring.security.entity.Cinema;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.CinemaMapper;
import spring.security.repository.CinemaRepository;
import spring.security.repository.RoomRepository;
import spring.security.service.CinemaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;
    private final CinemaMapper cinemaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CinemaResponse> getActiveCinemas() {
        return cinemaRepository.findAllByDeletedFalse()
                .stream()
                .map(cinemaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaResponse getCinemaById(Long id) {
        return cinemaRepository.findByIdAndDeletedFalse(id)
                .map(cinemaMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));
    }

    @Override
    @Transactional
    public CinemaResponse createCinema(CreateCinemaRequest request) {
        String normalizedName = request.getName().trim();
        if (cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse(normalizedName)) {
            throw new AppException(ErrorCode.CINEMA_ALREADY_EXISTS);
        }

        Cinema cinema = cinemaMapper.toEntity(request);
        cinema.setName(normalizedName);
        cinema.setAddress(request.getAddress().trim());
        Cinema savedCinema = cinemaRepository.save(cinema);

        return cinemaMapper.toResponse(savedCinema);
    }

    @Override
    @Transactional
    public CinemaResponse updateCinema(Long id, UpdateCinemaRequest request) {
        Cinema cinema = cinemaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        String normalizedName = request.getName().trim();
        if (cinemaRepository.existsByNameIgnoreCaseAndDeletedFalseAndIdNot(normalizedName, id)) {
            throw new AppException(ErrorCode.CINEMA_ALREADY_EXISTS);
        }

        cinemaMapper.updateEntityFromDto(request, cinema);
        cinema.setName(normalizedName);
        cinema.setAddress(request.getAddress().trim());
        Cinema updatedCinema = cinemaRepository.save(cinema);

        return cinemaMapper.toResponse(updatedCinema);
    }

    @Override
    @Transactional
    public void deleteCinema(Long id) {
        Cinema cinema = cinemaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        if (roomRepository.existsByCinemaIdAndDeletedFalse(id)) {
            throw new AppException(ErrorCode.CINEMA_HAS_ROOMS);
        }

        cinema.setDeleted(true);
        cinemaRepository.save(cinema);
    }
}
