package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.security.dto.request.CreateMovieRequest;
import spring.security.dto.request.UpdateMovieRequest;
import spring.security.dto.response.MovieResponse;
import spring.security.entity.Movie;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MovieMapper {
    MovieResponse toResponse(Movie movie);

    Movie toEntity(CreateMovieRequest request);

    void updateEntityFromDto(UpdateMovieRequest request, @MappingTarget Movie movie);
}
