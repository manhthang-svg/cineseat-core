package spring.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.response.DistrictResponse;
import spring.security.dto.response.ProvinceResponse;
import spring.security.dto.response.WardResponse;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.AddressService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void getProvinces_ReturnsList() throws Exception {
        ProvinceResponse province = ProvinceResponse.builder()
                .code("79")
                .name("Hồ Chí Minh")
                .fullName("Thành phố Hồ Chí Minh")
                .build();

        when(addressService.getProvinces()).thenReturn(List.of(province));

        mockMvc.perform(get("/api/addresses/provinces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("79"))
                .andExpect(jsonPath("$.data[0].name").value("Hồ Chí Minh"));

        verify(addressService).getProvinces();
    }

    @Test
    void getDistrictsByProvince_ReturnsList() throws Exception {
        String provinceCode = "79";
        DistrictResponse district = DistrictResponse.builder()
                .code("765")
                .name("Bình Thạnh")
                .fullName("Quận Bình Thạnh")
                .provinceCode(provinceCode)
                .build();

        when(addressService.getDistrictsByProvince(provinceCode)).thenReturn(List.of(district));

        mockMvc.perform(get("/api/addresses/provinces/{provinceCode}/districts", provinceCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("765"))
                .andExpect(jsonPath("$.data[0].name").value("Bình Thạnh"));

        verify(addressService).getDistrictsByProvince(provinceCode);
    }

    @Test
    void getDistrictsByProvince_NotFound_Returns404() throws Exception {
        String provinceCode = "999";
        when(addressService.getDistrictsByProvince(provinceCode))
                .thenThrow(new AppException(ErrorCode.PROVINCE_NOT_FOUND));

        mockMvc.perform(get("/api/addresses/provinces/{provinceCode}/districts", provinceCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.PROVINCE_NOT_FOUND.getCode()));
    }

    @Test
    void getWardsByDistrict_ReturnsList() throws Exception {
        String districtCode = "765";
        WardResponse ward = WardResponse.builder()
                .code("26830")
                .name("Phường 22")
                .fullName("Phường 22")
                .districtCode(districtCode)
                .build();

        when(addressService.getWardsByDistrict(districtCode)).thenReturn(List.of(ward));

        mockMvc.perform(get("/api/addresses/districts/{districtCode}/wards", districtCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("26830"))
                .andExpect(jsonPath("$.data[0].name").value("Phường 22"));

        verify(addressService).getWardsByDistrict(districtCode);
    }
}
