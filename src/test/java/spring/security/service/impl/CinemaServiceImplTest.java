package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.security.dto.request.CreateCinemaRequest;
import spring.security.dto.request.UpdateCinemaRequest;
import spring.security.dto.response.CinemaResponse;
import spring.security.entity.Cinema;
import spring.security.entity.District;
import spring.security.entity.Province;
import spring.security.entity.Ward;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.CinemaMapper;
import spring.security.repository.CinemaRepository;
import spring.security.repository.DistrictRepository;
import spring.security.repository.ProvinceRepository;
import spring.security.repository.RoomRepository;
import spring.security.repository.WardRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CinemaServiceImplTest {

    @Mock
    private CinemaRepository cinemaRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ProvinceRepository provinceRepository;
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private WardRepository wardRepository;
    @Mock
    private CinemaMapper cinemaMapper;

    private CinemaServiceImpl cinemaService;

    private Province province;
    private District district;
    private Ward ward;

    @BeforeEach
    void setUp() {
        cinemaService = new CinemaServiceImpl(
                cinemaRepository,
                roomRepository,
                provinceRepository,
                districtRepository,
                wardRepository,
                cinemaMapper
        );

        province = Province.builder()
                .code("79")
                .name("Hồ Chí Minh")
                .fullName("Thành phố Hồ Chí Minh")
                .build();

        district = District.builder()
                .code("765")
                .name("Bình Thạnh")
                .fullName("Quận Bình Thạnh")
                .province(province)
                .build();

        ward = Ward.builder()
                .code("26830")
                .name("Phường 22")
                .fullName("Phường 22")
                .district(district)
                .build();
    }

    @Test
    void getActiveCinemas_ReturnsList() {
        Cinema cinema = new Cinema();
        cinema.setId(1L);
        CinemaResponse response = CinemaResponse.builder().id(1L).name("CineVault Central").build();

        when(cinemaRepository.findAllByDeletedFalse()).thenReturn(List.of(cinema));
        when(cinemaMapper.toResponse(cinema)).thenReturn(response);

        List<CinemaResponse> result = cinemaService.getActiveCinemas();

        assertThat(result).containsExactly(response);
    }

    @Test
    void getCinemaById_Success() {
        Long cinemaId = 1L;
        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);
        CinemaResponse response = CinemaResponse.builder().id(cinemaId).name("CineVault Central").build();

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(cinema));
        when(cinemaMapper.toResponse(cinema)).thenReturn(response);

        CinemaResponse result = cinemaService.getCinemaById(cinemaId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getCinemaById_NotFound_ThrowsException() {
        Long cinemaId = 99L;
        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cinemaService.getCinemaById(cinemaId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CINEMA_NOT_FOUND);
    }

    @Test
    void createCinema_Success() {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("  CineVault Landmark  ")
                .provinceCode("79")
                .districtCode("765")
                .wardCode("26830")
                .detailAddress("  Tầng B1, Landmark 81  ")
                .build();

        Cinema entity = new Cinema();
        Cinema saved = new Cinema();
        saved.setId(1L);
        saved.setName("CineVault Landmark");
        CinemaResponse response = CinemaResponse.builder().id(1L).name("CineVault Landmark").build();

        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse("CineVault Landmark")).thenReturn(false);
        when(provinceRepository.findById("79")).thenReturn(Optional.of(province));
        when(districtRepository.findById("765")).thenReturn(Optional.of(district));
        when(wardRepository.findById("26830")).thenReturn(Optional.of(ward));
        when(cinemaMapper.toEntity(request)).thenReturn(entity);
        when(cinemaRepository.save(entity)).thenReturn(saved);
        when(cinemaMapper.toResponse(saved)).thenReturn(response);

        CinemaResponse result = cinemaService.createCinema(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("CineVault Landmark");
        assertThat(entity.getName()).isEqualTo("CineVault Landmark");
        assertThat(entity.getDetailAddress()).isEqualTo("Tầng B1, Landmark 81");
        assertThat(entity.getAddress()).isEqualTo("Tầng B1, Landmark 81, Phường 22, Quận Bình Thạnh, Thành phố Hồ Chí Minh");
        assertThat(entity.getProvince()).isEqualTo(province);
        assertThat(entity.getDistrict()).isEqualTo(district);
        assertThat(entity.getWard()).isEqualTo(ward);
        verify(cinemaRepository).save(entity);
    }

    @Test
    void createCinema_DuplicateName_ThrowsException() {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("CineVault Landmark")
                .provinceCode("79")
                .districtCode("765")
                .wardCode("26830")
                .detailAddress("Tầng B1, Landmark 81")
                .build();

        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse("CineVault Landmark")).thenReturn(true);

        assertThatThrownBy(() -> cinemaService.createCinema(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CINEMA_ALREADY_EXISTS);

        verify(cinemaRepository, never()).save(any());
    }

    @Test
    void createCinema_InvalidDistrictProvinceHierarchy_ThrowsException() {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("CineVault Landmark")
                .provinceCode("01") // Ha Noi
                .districtCode("765") // Binh Thanh in HCM
                .wardCode("26830")
                .detailAddress("Tầng B1")
                .build();

        Province hanoi = Province.builder().code("01").name("Hà Nội").fullName("Thành phố Hà Nội").build();

        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse("CineVault Landmark")).thenReturn(false);
        when(cinemaMapper.toEntity(request)).thenReturn(new Cinema());
        when(provinceRepository.findById("01")).thenReturn(Optional.of(hanoi));
        when(districtRepository.findById("765")).thenReturn(Optional.of(district));

        assertThatThrownBy(() -> cinemaService.createCinema(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_ADDRESS_HIERARCHY);

        verify(cinemaRepository, never()).save(any());
    }

    @Test
    void createCinema_InvalidWardDistrictHierarchy_ThrowsException() {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("CineVault Landmark")
                .provinceCode("79")
                .districtCode("765")
                .wardCode("00340") // Thuong Dinh in Thanh Xuan
                .detailAddress("Tầng B1")
                .build();

        District thanhXuan = District.builder().code("009").province(province).build();
        Ward thuongDinh = Ward.builder().code("00340").district(thanhXuan).build();

        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse("CineVault Landmark")).thenReturn(false);
        when(cinemaMapper.toEntity(request)).thenReturn(new Cinema());
        when(provinceRepository.findById("79")).thenReturn(Optional.of(province));
        when(districtRepository.findById("765")).thenReturn(Optional.of(district));
        when(wardRepository.findById("00340")).thenReturn(Optional.of(thuongDinh));

        assertThatThrownBy(() -> cinemaService.createCinema(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_ADDRESS_HIERARCHY);

        verify(cinemaRepository, never()).save(any());
    }

    @Test
    void updateCinema_Success() {
        Long cinemaId = 1L;
        UpdateCinemaRequest request = UpdateCinemaRequest.builder()
                .name("CineVault Landmark Updated")
                .provinceCode("79")
                .districtCode("765")
                .wardCode("26830")
                .detailAddress("456 Le Loi")
                .build();

        Cinema existing = new Cinema();
        existing.setId(cinemaId);
        Cinema saved = new Cinema();
        saved.setId(cinemaId);
        CinemaResponse response = CinemaResponse.builder().id(cinemaId).name("CineVault Landmark Updated").build();

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(existing));
        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalseAndIdNot("CineVault Landmark Updated", cinemaId)).thenReturn(false);
        when(provinceRepository.findById("79")).thenReturn(Optional.of(province));
        when(districtRepository.findById("765")).thenReturn(Optional.of(district));
        when(wardRepository.findById("26830")).thenReturn(Optional.of(ward));
        when(cinemaRepository.save(existing)).thenReturn(saved);
        when(cinemaMapper.toResponse(saved)).thenReturn(response);

        CinemaResponse result = cinemaService.updateCinema(cinemaId, request);

        assertThat(result).isNotNull();
        verify(cinemaMapper).updateEntityFromDto(request, existing);
        verify(cinemaRepository).save(existing);
        assertThat(existing.getAddress()).isEqualTo("456 Le Loi, Phường 22, Quận Bình Thạnh, Thành phố Hồ Chí Minh");
    }

    @Test
    void deleteCinema_Success() {
        Long cinemaId = 1L;
        Cinema existing = new Cinema();
        existing.setId(cinemaId);
        existing.setDeleted(false);

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(existing));
        when(roomRepository.existsByCinemaIdAndDeletedFalse(cinemaId)).thenReturn(false);

        cinemaService.deleteCinema(cinemaId);

        assertThat(existing.getDeleted()).isTrue();
        verify(cinemaRepository).save(existing);
    }

    @Test
    void deleteCinema_HasRooms_ThrowsException() {
        Long cinemaId = 1L;
        Cinema existing = new Cinema();
        existing.setId(cinemaId);

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(existing));
        when(roomRepository.existsByCinemaIdAndDeletedFalse(cinemaId)).thenReturn(true);

        assertThatThrownBy(() -> cinemaService.deleteCinema(cinemaId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CINEMA_HAS_ROOMS);

        verify(cinemaRepository, never()).save(any());
    }
}
