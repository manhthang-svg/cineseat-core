package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.entity.Showtime;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {

    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "room", ignore = true)
    Showtime toEntity(CreateShowtimeRequest request);

    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "roomId", source = "room.id")
    CreateShowtimeResponse toResponse(Showtime showtime);
}
