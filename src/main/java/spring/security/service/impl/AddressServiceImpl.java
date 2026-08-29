package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.response.DistrictResponse;
import spring.security.dto.response.ProvinceResponse;
import spring.security.dto.response.WardResponse;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.AddressMapper;
import spring.security.repository.DistrictRepository;
import spring.security.repository.ProvinceRepository;
import spring.security.repository.WardRepository;
import spring.security.service.AddressService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceResponse> getProvinces() {
        return provinceRepository.findAllByOrderByNameAsc()
                .stream()
                .map(addressMapper::toProvinceResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponse> getDistrictsByProvince(String provinceCode) {
        if (!provinceRepository.existsById(provinceCode)) {
            throw new AppException(ErrorCode.PROVINCE_NOT_FOUND);
        }
        return districtRepository.findAllByProvinceCodeOrderByNameAsc(provinceCode)
                .stream()
                .map(addressMapper::toDistrictResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardResponse> getWardsByDistrict(String districtCode) {
        if (!districtRepository.existsById(districtCode)) {
            throw new AppException(ErrorCode.DISTRICT_NOT_FOUND);
        }
        return wardRepository.findAllByDistrictCodeOrderByNameAsc(districtCode)
                .stream()
                .map(addressMapper::toWardResponse)
                .toList();
    }
}
