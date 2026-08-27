package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.dto.request.CreateSeatRequest;
import spring.security.dto.request.UpdateSeatRequest;
import spring.security.dto.response.SeatResponse;
import spring.security.entity.Room;
import spring.security.entity.Seat;
import spring.security.enums.ErrorCode;
import spring.security.enums.SeatType;
import spring.security.exceptions.AppException;
import spring.security.mapper.SeatMapper;
import spring.security.repository.BookingSeatRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.SeatRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private BookingSeatRepository bookingSeatRepository;
    @Mock
    private SeatMapper seatMapper;

    private SeatServiceImpl seatService;

    @BeforeEach
    void setUp() {
        seatService = new SeatServiceImpl(roomRepository, seatRepository, bookingSeatRepository, seatMapper);
    }

    @Test
    void createSeat_Success() {
        Long roomId = 10L;
        CreateSeatRequest request = CreateSeatRequest.builder()
                .rowLabel("  a  ")
                .seatNumber(1)
                .seatType(SeatType.VIP)
                .build();

        Room room = new Room();
        room.setId(roomId);

        Seat entity = new Seat();
        Seat saved = new Seat();
        saved.setId(100L);
        saved.setRowLabel("A");
        saved.setSeatNumber(1);
        saved.setSeatType(SeatType.VIP);
        saved.setRoom(room);

        SeatResponse response = SeatResponse.builder()
                .id(100L)
                .roomId(roomId)
                .rowLabel("A")
                .seatNumber(1)
                .seatCode("A1")
                .seatType(SeatType.VIP)
                .build();

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.of(room));
        when(seatRepository.existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalse(roomId, "A", 1)).thenReturn(false);
        when(seatMapper.toEntity(request)).thenReturn(entity);
        when(seatRepository.save(entity)).thenReturn(saved);
        when(seatMapper.toResponse(saved)).thenReturn(response);

        SeatResponse result = seatService.createSeat(roomId, request);

        assertThat(result).isNotNull();
        assertThat(result.getSeatCode()).isEqualTo("A1");
        assertThat(entity.getRowLabel()).isEqualTo("A");
        assertThat(entity.getRoom()).isEqualTo(room);
        verify(seatRepository).save(entity);
    }

    @Test
    void createSeat_RoomNotFound_ThrowsException() {
        Long roomId = 99L;
        CreateSeatRequest request = CreateSeatRequest.builder()
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.REGULAR)
                .build();

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatService.createSeat(roomId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ROOM_NOT_FOUND);

        verify(seatRepository, never()).save(any());
    }

    @Test
    void createSeat_DuplicateSeatInRoom_ThrowsException() {
        Long roomId = 10L;
        CreateSeatRequest request = CreateSeatRequest.builder()
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.REGULAR)
                .build();

        Room room = new Room();
        room.setId(roomId);

        when(roomRepository.findByIdAndDeletedFalse(roomId)).thenReturn(Optional.of(room));
        when(seatRepository.existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalse(roomId, "A", 1)).thenReturn(true);

        assertThatThrownBy(() -> seatService.createSeat(roomId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SEAT_ALREADY_EXISTS);

        verify(seatRepository, never()).save(any());
    }

    @Test
    void updateSeat_Success() {
        Long seatId = 100L;
        UpdateSeatRequest request = UpdateSeatRequest.builder()
                .rowLabel("b")
                .seatNumber(2)
                .seatType(SeatType.VIP)
                .build();

        Room room = new Room();
        room.setId(10L);

        Seat existing = new Seat();
        existing.setId(seatId);
        existing.setRowLabel("A");
        existing.setSeatNumber(1);
        existing.setRoom(room);

        Seat saved = new Seat();
        saved.setId(seatId);
        saved.setRowLabel("B");
        saved.setSeatNumber(2);

        SeatResponse response = SeatResponse.builder().id(seatId).seatCode("B2").build();

        when(seatRepository.findByIdAndDeletedFalse(seatId)).thenReturn(Optional.of(existing));
        when(seatRepository.existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalseAndIdNot(10L, "B", 2, seatId)).thenReturn(false);
        when(seatRepository.save(existing)).thenReturn(saved);
        when(seatMapper.toResponse(saved)).thenReturn(response);

        SeatResponse result = seatService.updateSeat(seatId, request);

        assertThat(result).isNotNull();
        assertThat(result.getSeatCode()).isEqualTo("B2");
        verify(seatMapper).updateEntityFromDto(request, existing);
        verify(seatRepository).save(existing);
    }

    @Test
    void deleteSeat_Success() {
        Long seatId = 100L;
        Seat existing = new Seat();
        existing.setId(seatId);
        existing.setDeleted(false);

        when(seatRepository.findByIdAndDeletedFalse(seatId)).thenReturn(Optional.of(existing));
        when(bookingSeatRepository.existsBySeatIdAndDeletedFalse(seatId)).thenReturn(false);

        seatService.deleteSeat(seatId);

        assertThat(existing.getDeleted()).isTrue();
        verify(seatRepository).save(existing);
    }

    @Test
    void deleteSeat_HasBookings_ThrowsException() {
        Long seatId = 100L;
        Seat existing = new Seat();
        existing.setId(seatId);

        when(seatRepository.findByIdAndDeletedFalse(seatId)).thenReturn(Optional.of(existing));
        when(bookingSeatRepository.existsBySeatIdAndDeletedFalse(seatId)).thenReturn(true);

        assertThatThrownBy(() -> seatService.deleteSeat(seatId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SEAT_HAS_BOOKINGS);

        verify(seatRepository, never()).save(any());
    }
}
