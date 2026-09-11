package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.dto.response.ShowtimeViewResponse;
import spring.security.entity.Showtime;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {

    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "room", ignore = true)
    Showtime toEntity(CreateShowtimeRequest request);

    @Mapping(target = "movieId", source = "movie.id")
    @Mapping(target = "roomId", source = "room.id")
    CreateShowtimeResponse toResponse(Showtime showtime);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomName", source = "room.name")
    @Mapping(target = "cinemaId", source = "room.cinema.id")
    @Mapping(target = "cinemaName", source = "room.cinema.name")
    @Mapping(target = "cinemaAddress", source = "room.cinema.address")
    ShowtimeViewResponse toViewResponse(Showtime showtime);
}
