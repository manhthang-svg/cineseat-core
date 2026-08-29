package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.dto.response.DistrictResponse;
import spring.security.dto.response.ProvinceResponse;
import spring.security.dto.response.WardResponse;
import spring.security.entity.District;
import spring.security.entity.Province;
import spring.security.entity.Ward;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.AddressMapper;
import spring.security.repository.DistrictRepository;
import spring.security.repository.ProvinceRepository;
import spring.security.repository.WardRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private ProvinceRepository provinceRepository;
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private WardRepository wardRepository;
    @Mock
    private AddressMapper addressMapper;

    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(
                provinceRepository,
                districtRepository,
                wardRepository,
                addressMapper
        );
    }

    @Test
    void getProvinces_ReturnsList() {
        Province province = Province.builder().code("79").name("Hồ Chí Minh").fullName("Thành phố Hồ Chí Minh").build();
        ProvinceResponse response = ProvinceResponse.builder().code("79").name("Hồ Chí Minh").fullName("Thành phố Hồ Chí Minh").build();

        when(provinceRepository.findAllByOrderByNameAsc()).thenReturn(List.of(province));
        when(addressMapper.toProvinceResponse(province)).thenReturn(response);

        List<ProvinceResponse> result = addressService.getProvinces();

        assertThat(result).containsExactly(response);
    }

    @Test
    void getDistrictsByProvince_Success_ReturnsList() {
        String provinceCode = "79";
        District district = District.builder().code("765").name("Bình Thạnh").fullName("Quận Bình Thạnh").build();
        DistrictResponse response = DistrictResponse.builder().code("765").name("Bình Thạnh").fullName("Quận Bình Thạnh").provinceCode("79").build();

        when(provinceRepository.existsById(provinceCode)).thenReturn(true);
        when(districtRepository.findAllByProvinceCodeOrderByNameAsc(provinceCode)).thenReturn(List.of(district));
        when(addressMapper.toDistrictResponse(district)).thenReturn(response);

        List<DistrictResponse> result = addressService.getDistrictsByProvince(provinceCode);

        assertThat(result).containsExactly(response);
    }

    @Test
    void getDistrictsByProvince_NotFound_ThrowsException() {
        String provinceCode = "999";
        when(provinceRepository.existsById(provinceCode)).thenReturn(false);

        assertThatThrownBy(() -> addressService.getDistrictsByProvince(provinceCode))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PROVINCE_NOT_FOUND);
    }

    @Test
    void getWardsByDistrict_Success_ReturnsList() {
        String districtCode = "765";
        Ward ward = Ward.builder().code("26830").name("Phường 22").fullName("Phường 22").build();
        WardResponse response = WardResponse.builder().code("26830").name("Phường 22").fullName("Phường 22").districtCode("765").build();

        when(districtRepository.existsById(districtCode)).thenReturn(true);
        when(wardRepository.findAllByDistrictCodeOrderByNameAsc(districtCode)).thenReturn(List.of(ward));
        when(addressMapper.toWardResponse(ward)).thenReturn(response);

        List<WardResponse> result = addressService.getWardsByDistrict(districtCode);

        assertThat(result).containsExactly(response);
    }

    @Test
    void getWardsByDistrict_NotFound_ThrowsException() {
        String districtCode = "999";
        when(districtRepository.existsById(districtCode)).thenReturn(false);

        assertThatThrownBy(() -> addressService.getWardsByDistrict(districtCode))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DISTRICT_NOT_FOUND);
    }
}
