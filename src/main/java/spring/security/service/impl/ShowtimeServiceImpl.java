package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.dto.response.ShowtimeSeatMapResponse;
import spring.security.dto.response.ShowtimeSeatResponse;
import spring.security.dto.response.ShowtimeViewResponse;
import spring.security.entity.Movie;
import spring.security.entity.Room;
import spring.security.entity.Seat;
import spring.security.entity.Showtime;
import spring.security.entity.ShowtimeSeat;
import spring.security.enums.ErrorCode;
import spring.security.enums.SeatType;
import spring.security.enums.ShowtimeSeatStatus;
import spring.security.exceptions.AppException;
import spring.security.mapper.ShowtimeMapper;
import spring.security.mapper.ShowtimeSeatMapper;
import spring.security.repository.MovieRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.SeatRepository;
import spring.security.repository.ShowtimeRepository;
import spring.security.repository.ShowtimeSeatRepository;
import spring.security.service.ShowtimeService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private static final Duration MAX_PUBLIC_SCHEDULE_RANGE = Duration.ofDays(31);

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeMapper showtimeMapper;
    private final ShowtimeSeatMapper showtimeSeatMapper;

    @Transactional
    @Override
    public CreateShowtimeResponse createShowtime(CreateShowtimeRequest request) {
        if (request.getStartTime() == null
                || request.getEndTime() == null
                || !request.getStartTime().isBefore(request.getEndTime())) {
            throw new AppException(ErrorCode.INVALID_SHOWTIME_TIME);
        }

        Room roomEntity = roomRepository.findByIdAndDeletedFalseForUpdate(
                request.getRoomId()).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        Movie movieEntity = movieRepository.findByIdAndDeletedFalse(
                request.getMovieId()).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        boolean exists = showtimeRepository.existsAnOverlappingShowtime(
                request.getRoomId(),
                null,
                request.getStartTime(),
                request.getEndTime()
        );

        if (exists) {
            throw new AppException(ErrorCode.OVERLAP_SHOWTIME_EXCEPTION);
        }
        Showtime showtime = showtimeMapper.toEntity(request);
        showtime.setMovie(movieEntity);
        showtime.setRoom(roomEntity);

        Showtime savedShowtime = showtimeRepository.save(showtime);

        // Auto-generate ShowtimeSeat inventory for all active seats in the room
        List<Seat> roomSeats = seatRepository.findAllByRoomIdAndDeletedFalse(roomEntity.getId());
        List<ShowtimeSeat> showtimeSeats = roomSeats.stream()
                .map(seat -> {
                    ShowtimeSeat showtimeSeat = new ShowtimeSeat();
                    showtimeSeat.setShowtime(savedShowtime);
                    showtimeSeat.setSeat(seat);
                    showtimeSeat.setStatus(ShowtimeSeatStatus.AVAILABLE);
                    showtimeSeat.setPrice(determineSeatPrice(seat.getSeatType()));
                    return showtimeSeat;
                })
                .toList();
        showtimeSeatRepository.saveAll(showtimeSeats);

        return showtimeMapper.toResponse(savedShowtime);
    }

    private BigDecimal determineSeatPrice(SeatType seatType) {
        if (seatType == null) {
            return BigDecimal.valueOf(90000);
        }
        return switch (seatType) {
            case VIP -> BigDecimal.valueOf(120000);
            case COUPLE -> BigDecimal.valueOf(180000);
            case REGULAR -> BigDecimal.valueOf(90000);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeViewResponse> getMovieShowtimes(Long movieId, Instant from, Instant to) {
        if (from == null
                || to == null
                || !from.isBefore(to)
                || Duration.between(from, to).compareTo(MAX_PUBLIC_SCHEDULE_RANGE) > 0) {
            throw new AppException(ErrorCode.INVALID_SHOWTIME_RANGE);
        }

        if (!movieRepository.existsByIdAndDeletedFalse(movieId)) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }

        return showtimeRepository.findAvailableByMovieAndStartTimeBetween(movieId, from, to)
                .stream()
                .map(showtimeMapper::toViewResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShowtimeSeatMapResponse getShowtimeSeats(Long showtimeId) {
        Showtime showtime = showtimeRepository.findByIdWithDetails(showtimeId)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findByShowtimeIdWithSeatOrderByPosition(showtimeId);

        List<ShowtimeSeatResponse> seatResponses = showtimeSeats.stream()
                .map(showtimeSeatMapper::toResponse)
                .toList();

        int totalSeats = seatResponses.size();
        int availableSeats = (int) seatResponses.stream()
                .filter(seat -> seat.getStatus() == ShowtimeSeatStatus.AVAILABLE)
                .count();

        return ShowtimeSeatMapResponse.builder()
                .showtimeId(showtime.getId())
                .movieTitle(showtime.getMovie().getTitle())
                .cinemaName(showtime.getRoom().getCinema().getName())
                .roomName(showtime.getRoom().getName())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .seats(seatResponses)
                .build();
    }
}
