package spring.security.service;

import spring.security.dto.request.CreateSeatRequest;
import spring.security.dto.request.UpdateSeatRequest;
import spring.security.dto.response.SeatResponse;

public interface SeatService {
    java.util.List<SeatResponse> getSeatsByRoomId(Long roomId);

    SeatResponse createSeat(Long roomId, CreateSeatRequest request);

    SeatResponse updateSeat(Long id, UpdateSeatRequest request);

    void deleteSeat(Long id);
}
