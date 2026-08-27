package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.security.dto.request.CreateSeatRequest;
import spring.security.dto.request.UpdateSeatRequest;
import spring.security.dto.response.SeatResponse;
import spring.security.entity.Seat;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SeatMapper {
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(target = "seatCode", expression = "java(seat.getRowLabel() + seat.getSeatNumber())")
    SeatResponse toResponse(Seat seat);

    Seat toEntity(CreateSeatRequest request);

    void updateEntityFromDto(UpdateSeatRequest request, @MappingTarget Seat seat);
}
