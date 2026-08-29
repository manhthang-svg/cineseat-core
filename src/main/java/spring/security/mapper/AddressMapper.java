package spring.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import spring.security.dto.response.DistrictResponse;
import spring.security.dto.response.ProvinceResponse;
import spring.security.dto.response.WardResponse;
import spring.security.entity.District;
import spring.security.entity.Province;
import spring.security.entity.Ward;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AddressMapper {
    ProvinceResponse toProvinceResponse(Province province);

    @Mapping(target = "provinceCode", source = "province.code")
    DistrictResponse toDistrictResponse(District district);

    @Mapping(target = "districtCode", source = "district.code")
    WardResponse toWardResponse(Ward ward);
}
