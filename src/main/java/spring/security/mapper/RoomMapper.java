package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.security.dto.request.CreateRoomRequest;
import spring.security.dto.request.UpdateRoomRequest;
import spring.security.dto.response.RoomResponse;
import spring.security.entity.Room;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoomMapper {
    @Mapping(source = "cinema.id", target = "cinemaId")
    @Mapping(source = "cinema.name", target = "cinemaName")
    RoomResponse toResponse(Room room);

    Room toEntity(CreateRoomRequest request);

    void updateEntityFromDto(UpdateRoomRequest request, @MappingTarget Room room);
}
