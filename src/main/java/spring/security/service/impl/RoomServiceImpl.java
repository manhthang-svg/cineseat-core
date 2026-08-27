package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import spring.security.service.RoomService;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public java.util.List<RoomResponse> getRoomsByCinemaId(Long cinemaId) {
        if (!cinemaRepository.existsById(cinemaId)) {
            throw new AppException(ErrorCode.CINEMA_NOT_FOUND);
        }
        return roomRepository.findAllByCinemaIdAndDeletedFalse(cinemaId)
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RoomResponse createRoom(Long cinemaId, CreateRoomRequest request) {
        Cinema cinema = cinemaRepository.findByIdAndDeletedFalse(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        String normalizedName = request.getName().trim();
        if (roomRepository.existsByCinemaIdAndNameIgnoreCaseAndDeletedFalse(cinemaId, normalizedName)) {
            throw new AppException(ErrorCode.ROOM_ALREADY_EXISTS);
        }

        Room room = roomMapper.toEntity(request);
        room.setName(normalizedName);
        room.setCinema(cinema);
        Room savedRoom = roomRepository.save(room);

        return roomMapper.toResponse(savedRoom);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {
        Room room = roomRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        String normalizedName = request.getName().trim();
        Long cinemaId = room.getCinema().getId();
        if (roomRepository.existsByCinemaIdAndNameIgnoreCaseAndDeletedFalseAndIdNot(cinemaId, normalizedName, id)) {
            throw new AppException(ErrorCode.ROOM_ALREADY_EXISTS);
        }

        roomMapper.updateEntityFromDto(request, room);
        room.setName(normalizedName);
        Room updatedRoom = roomRepository.save(room);

        return roomMapper.toResponse(updatedRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        if (seatRepository.existsByRoomIdAndDeletedFalse(id)) {
            throw new AppException(ErrorCode.ROOM_HAS_SEATS);
        }

        if (showtimeRepository.existsByRoomIdAndDeletedFalse(id)) {
            throw new AppException(ErrorCode.ROOM_HAS_SHOWTIMES);
        }

        room.setDeleted(true);
        roomRepository.save(room);
    }
}
