package spring.security.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.dto.response.ShowtimeViewResponse;
import spring.security.entity.Movie;
import spring.security.entity.Room;
import spring.security.entity.Showtime;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.ShowtimeMapper;
import spring.security.repository.MovieRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.ShowtimeRepository;
import spring.security.service.ShowtimeService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {
    private static final Duration MAX_PUBLIC_SCHEDULE_RANGE = Duration.ofDays(31);

    private final ShowtimeRepository showtimeRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeMapper showtimeMapper;

    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository, RoomRepository roomRepository, MovieRepository movieRepository, ShowtimeMapper showtimeMapper) {
        this.showtimeRepository = showtimeRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.showtimeMapper = showtimeMapper;
    }


    @Transactional
    @Override
    public CreateShowtimeResponse createShowtime(CreateShowtimeRequest request) {
        if (request.getStartTime() == null
                || request.getEndTime() == null
                || !request.getStartTime().isBefore(request.getEndTime())) {
            throw new AppException(ErrorCode.INVALID_SHOWTIME_TIME);
        }

        Room roomEntity = roomRepository.findByIdAndDeletedFalseForUpdate(
                request.getRoomId()).orElseThrow(()->  new AppException(ErrorCode.ROOM_NOT_FOUND));
        Movie movieEntity = movieRepository.findByIdAndDeletedFalse(
                request.getMovieId()).orElseThrow(()->new AppException(ErrorCode.MOVIE_NOT_FOUND));
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

        return showtimeMapper.toResponse(savedShowtime);
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
}
