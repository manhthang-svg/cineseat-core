package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.dto.request.CreateRoomRequest;
import spring.security.dto.request.UpdateRoomRequest;
import spring.security.dto.response.RoomResponse;
import spring.security.entity.Cinema;
import spring.security.entity.Room;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.RoomMapper;
import spring.security.repository.CinemaRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.SeatRepository;
import spring.security.repository.ShowtimeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private CinemaRepository cinemaRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;
    @Mock
    private RoomMapper roomMapper;

    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomServiceImpl(cinemaRepository, roomRepository, seatRepository, showtimeRepository, roomMapper);
    }

    @Test
    void createRoom_Success() {
        Long cinemaId = 1L;
        CreateRoomRequest request = CreateRoomRequest.builder().name("  IMAX 01  ").build();

        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);

        Room entity = new Room();
        Room saved = new Room();
        saved.setId(10L);
        saved.setName("IMAX 01");
        saved.setCinema(cinema);

        RoomResponse response = RoomResponse.builder().id(10L).name("IMAX 01").cinemaId(cinemaId).build();

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(cinema));
        when(roomRepository.existsByCinemaIdAndNameIgnoreCaseAndDeletedFalse(cinemaId, "IMAX 01")).thenReturn(false);
        when(roomMapper.toEntity(request)).thenReturn(entity);
        when(roomRepository.save(entity)).thenReturn(saved);
        when(roomMapper.toResponse(saved)).thenReturn(response);

        RoomResponse result = roomService.createRoom(cinemaId, request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("IMAX 01");
        assertThat(entity.getCinema()).isEqualTo(cinema);
        verify(roomRepository).save(entity);
    }

    @Test
    void createRoom_CinemaNotFound_ThrowsException() {
        Long cinemaId = 99L;
        CreateRoomRequest request = CreateRoomRequest.builder().name("Room 1").build();

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.createRoom(cinemaId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CINEMA_NOT_FOUND);

        verify(roomRepository, never()).save(any());
    }

    @Test
    void createRoom_DuplicateNameInCinema_ThrowsException() {
        Long cinemaId = 1L;
        CreateRoomRequest request = CreateRoomRequest.builder().name("Room 1").build();
        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(cinema));
        when(roomRepository.existsByCinemaIdAndNameIgnoreCaseAndDeletedFalse(cinemaId, "Room 1")).thenReturn(true);

        assertThatThrownBy(() -> roomService.createRoom(cinemaId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ROOM_ALREADY_EXISTS);

        verify(roomRepository, never()).save(any());
    }

    @Test
    void updateRoom_Success() {
        Long roomId = 10L;
        UpdateRoomRequest request = UpdateRoomRequest.builder().name("IMAX Laser").build();

        Cinema cinema = new Cinema();
        cinema.setId(1L);

        Room existing = new Room();
        existing.setId(roomId);
        existing.setName("IMAX");
        existing.setCinema(cinema);

        Room saved = new Room();
        saved.setId(roomId);
        saved.setName("IMAX Laser");

        RoomResponse response = RoomResponse.builder().id(roomId).name("IMAX Laser").build();

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.of(existing));
        when(roomRepository.existsByCinemaIdAndNameIgnoreCaseAndDeletedFalseAndIdNot(1L, "IMAX Laser", roomId)).thenReturn(false);
        when(roomRepository.save(existing)).thenReturn(saved);
        when(roomMapper.toResponse(saved)).thenReturn(response);

        RoomResponse result = roomService.updateRoom(roomId, request);

        assertThat(result).isNotNull();
        verify(roomMapper).updateEntityFromDto(request, existing);
        verify(roomRepository).save(existing);
    }

    @Test
    void deleteRoom_Success() {
        Long roomId = 10L;
        Room existing = new Room();
        existing.setId(roomId);
        existing.setDeleted(false);

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.of(existing));
        when(seatRepository.existsByRoomIdAndDeletedFalse(roomId)).thenReturn(false);
        when(showtimeRepository.existsByRoomIdAndDeletedFalse(roomId)).thenReturn(false);

        roomService.deleteRoom(roomId);

        assertThat(existing.getDeleted()).isTrue();
        verify(roomRepository).save(existing);
    }

    @Test
    void deleteRoom_HasSeats_ThrowsException() {
        Long roomId = 10L;
        Room existing = new Room();
        existing.setId(roomId);

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.of(existing));
        when(seatRepository.existsByRoomIdAndDeletedFalse(roomId)).thenReturn(true);

        assertThatThrownBy(() -> roomService.deleteRoom(roomId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ROOM_HAS_SEATS);

        verify(roomRepository, never()).save(any());
    }

    @Test
    void deleteRoom_HasShowtimes_ThrowsException() {
        Long roomId = 10L;
        Room existing = new Room();
        existing.setId(roomId);

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.of(existing));
        when(seatRepository.existsByRoomIdAndDeletedFalse(roomId)).thenReturn(false);
        when(showtimeRepository.existsByRoomIdAndDeletedFalse(roomId)).thenReturn(true);

        assertThatThrownBy(() -> roomService.deleteRoom(roomId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ROOM_HAS_SHOWTIMES);

        verify(roomRepository, never()).save(any());
    }
}
