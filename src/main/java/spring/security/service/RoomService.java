package spring.security.service;

import spring.security.dto.request.CreateRoomRequest;
import spring.security.dto.request.UpdateRoomRequest;
import spring.security.dto.response.RoomResponse;

public interface RoomService {
    java.util.List<RoomResponse> getRoomsByCinemaId(Long cinemaId);

    RoomResponse createRoom(Long cinemaId, CreateRoomRequest request);

    RoomResponse updateRoom(Long id, UpdateRoomRequest request);

    void deleteRoom(Long id);
}
