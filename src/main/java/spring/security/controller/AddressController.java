package spring.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.DistrictResponse;
import spring.security.dto.response.ProvinceResponse;
import spring.security.dto.response.WardResponse;
import spring.security.service.AddressService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "Public administrative divisions / address APIs")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/provinces")
    @Operation(summary = "Get list of all provinces/cities")
    public ApiResponse<List<ProvinceResponse>> getProvinces() {
        return ApiResponse.success(addressService.getProvinces());
    }

    @GetMapping("/provinces/{provinceCode}/districts")
    @Operation(summary = "Get list of districts by province code")
    public ApiResponse<List<DistrictResponse>> getDistrictsByProvince(@PathVariable String provinceCode) {
        return ApiResponse.success(addressService.getDistrictsByProvince(provinceCode));
    }

    @GetMapping("/districts/{districtCode}/wards")
    @Operation(summary = "Get list of wards by district code")
    public ApiResponse<List<WardResponse>> getWardsByDistrict(@PathVariable String districtCode) {
        return ApiResponse.success(addressService.getWardsByDistrict(districtCode));
    }
}
