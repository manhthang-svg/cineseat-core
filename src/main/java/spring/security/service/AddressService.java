package spring.security.service;

import spring.security.dto.response.DistrictResponse;
import spring.security.dto.response.ProvinceResponse;
import spring.security.dto.response.WardResponse;

import java.util.List;

public interface AddressService {
    List<ProvinceResponse> getProvinces();

    List<DistrictResponse> getDistrictsByProvince(String provinceCode);

    List<WardResponse> getWardsByDistrict(String districtCode);
}
