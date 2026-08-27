package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.request.CreateSeatRequest;
import spring.security.dto.request.UpdateSeatRequest;
import spring.security.dto.response.SeatResponse;
import spring.security.entity.Room;
import spring.security.entity.Seat;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.SeatMapper;
import spring.security.repository.BookingSeatRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.SeatRepository;
import spring.security.service.SeatService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatMapper seatMapper;

    @Override
    @Transactional(readOnly = true)
    public java.util.List<SeatResponse> getSeatsByRoomId(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new AppException(ErrorCode.ROOM_NOT_FOUND);
        }
        return seatRepository.findAllByRoomIdAndDeletedFalse(roomId)
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SeatResponse createSeat(Long roomId, CreateSeatRequest request) {
        Room room = roomRepository.findByIdAndDeletedFalse(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        String normalizedRowLabel = request.getRowLabel().trim().toUpperCase(Locale.ROOT);
        if (seatRepository.existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalse(
                roomId, normalizedRowLabel, request.getSeatNumber())) {
            throw new AppException(ErrorCode.SEAT_ALREADY_EXISTS);
        }

        Seat seat = seatMapper.toEntity(request);
        seat.setRowLabel(normalizedRowLabel);
        seat.setRoom(room);
        Seat savedSeat = seatRepository.save(seat);

        return seatMapper.toResponse(savedSeat);
    }

    @Override
    @Transactional
    public SeatResponse updateSeat(Long id, UpdateSeatRequest request) {
        Seat seat = seatRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

        String normalizedRowLabel = request.getRowLabel().trim().toUpperCase(Locale.ROOT);
        Long roomId = seat.getRoom().getId();
        if (seatRepository.existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalseAndIdNot(
                roomId, normalizedRowLabel, request.getSeatNumber(), id)) {
            throw new AppException(ErrorCode.SEAT_ALREADY_EXISTS);
        }

        seatMapper.updateEntityFromDto(request, seat);
        seat.setRowLabel(normalizedRowLabel);
        Seat updatedSeat = seatRepository.save(seat);

        return seatMapper.toResponse(updatedSeat);
    }

    @Override
    @Transactional
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

        if (bookingSeatRepository.existsBySeatIdAndDeletedFalse(id)) {
            throw new AppException(ErrorCode.SEAT_HAS_BOOKINGS);
        }

        seat.setDeleted(true);
        seatRepository.save(seat);
    }
}
