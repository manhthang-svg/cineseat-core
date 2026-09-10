package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.entity.Movie;
import spring.security.entity.Room;
import spring.security.entity.Showtime;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.ShowtimeMapper;
import spring.security.repository.MovieRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.ShowtimeRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowtimeServiceImplTest {

    @Mock
    private ShowtimeRepository showtimeRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowtimeMapper showtimeMapper;

    private ShowtimeServiceImpl showtimeService;

    @BeforeEach
    void setUp() {
        showtimeService = new ShowtimeServiceImpl(
                showtimeRepository,
                roomRepository,
                movieRepository,
                showtimeMapper
        );
    }

    @Test
    void createShowtime_Success() {
        CreateShowtimeRequest request = request(
                "2026-09-20T03:00:00Z",
                "2026-09-20T05:00:00Z"
        );
        Room room = new Room();
        room.setId(2L);
        Movie movie = new Movie();
        movie.setId(1L);
        Showtime entity = new Showtime();
        Showtime saved = new Showtime();
        saved.setId(10L);
        saved.setRoom(room);
        saved.setMovie(movie);
        saved.setStartTime(request.getStartTime());
        saved.setEndTime(request.getEndTime());
        CreateShowtimeResponse response = CreateShowtimeResponse.builder()
                .id(10L)
                .movieId(1L)
                .roomId(2L)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        when(roomRepository.findByIdAndDeletedFalseForUpdate(2L)).thenReturn(Optional.of(room));
        when(movieRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.existsAnOverlappingShowtime(
                2L, null, request.getStartTime(), request.getEndTime()
        )).thenReturn(false);
        when(showtimeMapper.toEntity(request)).thenReturn(entity);
        when(showtimeRepository.save(entity)).thenReturn(saved);
        when(showtimeMapper.toResponse(saved)).thenReturn(response);

        CreateShowtimeResponse result = showtimeService.createShowtime(request);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(entity.getRoom()).isSameAs(room);
        assertThat(entity.getMovie()).isSameAs(movie);
        verify(showtimeRepository).save(entity);
    }

    @Test
    void createShowtime_InvalidTime_ThrowsBeforeDatabaseAccess() {
        CreateShowtimeRequest request = request(
                "2026-09-20T05:00:00Z",
                "2026-09-20T03:00:00Z"
        );

        assertThatThrownBy(() -> showtimeService.createShowtime(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_SHOWTIME_TIME);

        verify(roomRepository, never()).findByIdAndDeletedFalseForUpdate(any());
    }

    @Test
    void createShowtime_Overlapping_ThrowsConflictAndDoesNotSave() {
        CreateShowtimeRequest request = request(
                "2026-09-20T03:00:00Z",
                "2026-09-20T05:00:00Z"
        );
        Room room = new Room();
        Movie movie = new Movie();

        when(roomRepository.findByIdAndDeletedFalseForUpdate(2L)).thenReturn(Optional.of(room));
        when(movieRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.existsAnOverlappingShowtime(
                2L, null, request.getStartTime(), request.getEndTime()
        )).thenReturn(true);

        assertThatThrownBy(() -> showtimeService.createShowtime(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OVERLAP_SHOWTIME_EXCEPTION);

        verify(showtimeRepository, never()).save(any());
    }

    private CreateShowtimeRequest request(String startTime, String endTime) {
        CreateShowtimeRequest request = new CreateShowtimeRequest();
        request.setMovieId(1L);
        request.setRoomId(2L);
        request.setStartTime(Instant.parse(startTime));
        request.setEndTime(Instant.parse(endTime));
        return request;
    }
}
