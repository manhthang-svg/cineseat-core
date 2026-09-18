package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.security.dto.response.ShowtimeSeatResponse;
import spring.security.entity.ShowtimeSeat;

@Mapper(componentModel = "spring")
public interface ShowtimeSeatMapper {

    @Mapping(target = "seatId", source = "seat.id")
    @Mapping(target = "rowLabel", source = "seat.rowLabel")
    @Mapping(target = "seatNumber", source = "seat.seatNumber")
    @Mapping(target = "seatType", source = "seat.seatType")
    ShowtimeSeatResponse toResponse(ShowtimeSeat showtimeSeat);
}
