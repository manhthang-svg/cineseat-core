package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.dto.response.ShowtimeSeatMapResponse;
import spring.security.dto.response.ShowtimeSeatResponse;
import spring.security.dto.response.ShowtimeViewResponse;
import spring.security.entity.Cinema;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
    private ShowtimeSeatRepository showtimeSeatRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowtimeMapper showtimeMapper;
    @Mock
    private ShowtimeSeatMapper showtimeSeatMapper;

    private ShowtimeServiceImpl showtimeService;

    @BeforeEach
    void setUp() {
        showtimeService = new ShowtimeServiceImpl(
                showtimeRepository,
                showtimeSeatRepository,
                seatRepository,
                roomRepository,
                movieRepository,
                showtimeMapper,
                showtimeSeatMapper
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

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatType(SeatType.VIP);

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
        when(seatRepository.findAllByRoomIdAndDeletedFalse(2L)).thenReturn(List.of(seat));
        when(showtimeMapper.toResponse(saved)).thenReturn(response);

        CreateShowtimeResponse result = showtimeService.createShowtime(request);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(entity.getRoom()).isSameAs(room);
        assertThat(entity.getMovie()).isSameAs(movie);
        verify(showtimeRepository).save(entity);
        verify(seatRepository).findAllByRoomIdAndDeletedFalse(2L);
        verify(showtimeSeatRepository).saveAll(any());
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
    void createShowtime_OverlappingShowtime_ThrowsConflictError() {
        CreateShowtimeRequest request = request(
                "2026-09-20T03:00:00Z",
                "2026-09-20T05:00:00Z"
        );
        Room room = new Room();
        room.setId(2L);
        Movie movie = new Movie();
        movie.setId(1L);

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

    @Test
    void createShowtime_RoomNotFound_Throws404Error() {
        CreateShowtimeRequest request = request(
                "2026-09-20T03:00:00Z",
                "2026-09-20T05:00:00Z"
        );

        when(roomRepository.findByIdAndDeletedFalseForUpdate(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showtimeService.createShowtime(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ROOM_NOT_FOUND);

        verify(movieRepository, never()).findByIdAndDeletedFalse(any());
    }

    @Test
    void createShowtime_MovieNotFound_Throws404Error() {
        CreateShowtimeRequest request = request(
                "2026-09-20T03:00:00Z",
                "2026-09-20T05:00:00Z"
        );
        Room room = new Room();
        room.setId(2L);

        when(roomRepository.findByIdAndDeletedFalseForUpdate(2L)).thenReturn(Optional.of(room));
        when(movieRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showtimeService.createShowtime(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_NOT_FOUND);

        verify(showtimeRepository, never()).existsAnOverlappingShowtime(any(), any(), any(), any());
    }

    @Test
    void getMovieShowtimes_Success() {
        Instant from = Instant.parse("2026-09-20T00:00:00Z");
        Instant to = Instant.parse("2026-09-27T00:00:00Z");
        Showtime showtime = new Showtime();
        ShowtimeViewResponse response = ShowtimeViewResponse.builder()
                .id(1L)
                .startTime(Instant.parse("2026-09-20T03:00:00Z"))
                .endTime(Instant.parse("2026-09-20T05:00:00Z"))
                .roomId(2L)
                .roomName("Cinema 1")
                .cinemaId(3L)
                .cinemaName("CGV")
                .cinemaAddress("Address")
                .build();

        when(movieRepository.existsByIdAndDeletedFalse(1L)).thenReturn(true);
        when(showtimeRepository.findAvailableByMovieAndStartTimeBetween(1L, from, to))
                .thenReturn(List.of(showtime));
        when(showtimeMapper.toViewResponse(showtime)).thenReturn(response);

        List<ShowtimeViewResponse> result = showtimeService.getMovieShowtimes(1L, from, to);

        assertThat(result).containsExactly(response);
    }

    @Test
    void getMovieShowtimes_MovieNotFound_Throws404Error() {
        Instant from = Instant.parse("2026-09-20T00:00:00Z");
        Instant to = Instant.parse("2026-09-27T00:00:00Z");
        when(movieRepository.existsByIdAndDeletedFalse(99L)).thenReturn(false);

        assertThatThrownBy(() -> showtimeService.getMovieShowtimes(99L, from, to))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_NOT_FOUND);

        verify(showtimeRepository, never()).findAvailableByMovieAndStartTimeBetween(any(), any(), any());
    }

    @Test
    void getMovieShowtimes_RangeLongerThan31Days_ThrowsBeforeDatabaseAccess() {
        Instant from = Instant.parse("2026-09-01T00:00:00Z");
        Instant to = Instant.parse("2026-10-03T00:00:00Z");

        assertThatThrownBy(() -> showtimeService.getMovieShowtimes(1L, from, to))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_SHOWTIME_RANGE);

        verify(movieRepository, never()).existsByIdAndDeletedFalse(any());
    }

    @Test
    void getShowtimeSeats_Success() {
        Movie movie = new Movie();
        movie.setTitle("Khoảng Trời Rực Rỡ");

        Cinema cinema = new Cinema();
        cinema.setName("CGV Landmark 81");

        Room room = new Room();
        room.setName("Screen 01");
        room.setCinema(cinema);

        Showtime showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStartTime(Instant.parse("2026-09-20T19:00:00Z"));
        showtime.setEndTime(Instant.parse("2026-09-20T21:15:00Z"));

        ShowtimeSeat showtimeSeat = new ShowtimeSeat();
        showtimeSeat.setId(101L);
        showtimeSeat.setStatus(ShowtimeSeatStatus.AVAILABLE);

        ShowtimeSeatResponse seatResponse = ShowtimeSeatResponse.builder()
                .id(101L)
                .seatId(12L)
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.REGULAR)
                .price(BigDecimal.valueOf(90000))
                .status(ShowtimeSeatStatus.AVAILABLE)
                .build();

        when(showtimeRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(showtime));
        when(showtimeSeatRepository.findByShowtimeIdWithSeatOrderByPosition(1L)).thenReturn(List.of(showtimeSeat));
        when(showtimeSeatMapper.toResponse(showtimeSeat)).thenReturn(seatResponse);

        ShowtimeSeatMapResponse result = showtimeService.getShowtimeSeats(1L);

        assertThat(result.getShowtimeId()).isEqualTo(1L);
        assertThat(result.getMovieTitle()).isEqualTo("Khoảng Trời Rực Rỡ");
        assertThat(result.getCinemaName()).isEqualTo("CGV Landmark 81");
        assertThat(result.getRoomName()).isEqualTo("Screen 01");
        assertThat(result.getTotalSeats()).isEqualTo(1);
        assertThat(result.getAvailableSeats()).isEqualTo(1);
        assertThat(result.getSeats()).containsExactly(seatResponse);
    }

    @Test
    void getShowtimeSeats_ShowtimeNotFound_Throws404Error() {
        when(showtimeRepository.findByIdWithDetails(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showtimeService.getShowtimeSeats(99L))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SHOWTIME_NOT_FOUND);

        verify(showtimeSeatRepository, never()).findByShowtimeIdWithSeatOrderByPosition(any());
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
