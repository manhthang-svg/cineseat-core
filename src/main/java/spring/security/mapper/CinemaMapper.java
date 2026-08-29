package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.security.dto.request.CreateCinemaRequest;
import spring.security.dto.request.UpdateCinemaRequest;
import spring.security.dto.response.CinemaResponse;
import spring.security.entity.Cinema;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CinemaMapper {

    @Mapping(target = "provinceCode", source = "province.code")
    @Mapping(target = "provinceName", source = "province.fullName")
    @Mapping(target = "districtCode", source = "district.code")
    @Mapping(target = "districtName", source = "district.fullName")
    @Mapping(target = "wardCode", source = "ward.code")
    @Mapping(target = "wardName", source = "ward.fullName")
    CinemaResponse toResponse(Cinema cinema);

    @Mapping(target = "province", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "ward", ignore = true)
    @Mapping(target = "address", ignore = true)
    Cinema toEntity(CreateCinemaRequest request);

    @Mapping(target = "province", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "ward", ignore = true)
    @Mapping(target = "address", ignore = true)
    void updateEntityFromDto(UpdateCinemaRequest request, @MappingTarget Cinema cinema);
}
