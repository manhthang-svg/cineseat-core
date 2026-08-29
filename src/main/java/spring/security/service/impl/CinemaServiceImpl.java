package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import spring.security.service.CinemaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final CinemaMapper cinemaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CinemaResponse> getActiveCinemas() {
        return cinemaRepository.findAllByDeletedFalse()
                .stream()
                .map(cinemaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaResponse getCinemaById(Long id) {
        return cinemaRepository.findByIdAndDeletedFalse(id)
                .map(cinemaMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));
    }

    @Override
    @Transactional
    public CinemaResponse createCinema(CreateCinemaRequest request) {
        String normalizedName = request.getName().trim();
        if (cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse(normalizedName)) {
            throw new AppException(ErrorCode.CINEMA_ALREADY_EXISTS);
        }

        Cinema cinema = cinemaMapper.toEntity(request);
        cinema.setName(normalizedName);
        validateAndApplyAddress(
                request.getProvinceCode(),
                request.getDistrictCode(),
                request.getWardCode(),
                request.getDetailAddress(),
                cinema
        );
        Cinema savedCinema = cinemaRepository.save(cinema);

        return cinemaMapper.toResponse(savedCinema);
    }

    @Override
    @Transactional
    public CinemaResponse updateCinema(Long id, UpdateCinemaRequest request) {
        Cinema cinema = cinemaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        String normalizedName = request.getName().trim();
        if (cinemaRepository.existsByNameIgnoreCaseAndDeletedFalseAndIdNot(normalizedName, id)) {
            throw new AppException(ErrorCode.CINEMA_ALREADY_EXISTS);
        }

        cinemaMapper.updateEntityFromDto(request, cinema);
        cinema.setName(normalizedName);
        validateAndApplyAddress(
                request.getProvinceCode(),
                request.getDistrictCode(),
                request.getWardCode(),
                request.getDetailAddress(),
                cinema
        );
        Cinema updatedCinema = cinemaRepository.save(cinema);

        return cinemaMapper.toResponse(updatedCinema);
    }

    @Override
    @Transactional
    public void deleteCinema(Long id) {
        Cinema cinema = cinemaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        if (roomRepository.existsByCinemaIdAndDeletedFalse(id)) {
            throw new AppException(ErrorCode.CINEMA_HAS_ROOMS);
        }

        cinema.setDeleted(true);
        cinemaRepository.save(cinema);
    }

    private void validateAndApplyAddress(
            String provinceCode,
            String districtCode,
            String wardCode,
            String detailAddress,
            Cinema cinema
    ) {
        Province province = provinceRepository.findById(provinceCode)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND));

        District district = districtRepository.findById(districtCode)
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_FOUND));

        if (!district.getProvince().getCode().equals(province.getCode())) {
            throw new AppException(ErrorCode.INVALID_ADDRESS_HIERARCHY);
        }

        Ward ward = wardRepository.findById(wardCode)
                .orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_FOUND));

        if (!ward.getDistrict().getCode().equals(district.getCode())) {
            throw new AppException(ErrorCode.INVALID_ADDRESS_HIERARCHY);
        }

        String normalizedDetail = detailAddress.trim();
        String fullAddress = String.format("%s, %s, %s, %s",
                normalizedDetail,
                ward.getFullName(),
                district.getFullName(),
                province.getFullName());

        cinema.setProvince(province);
        cinema.setDistrict(district);
        cinema.setWard(ward);
        cinema.setDetailAddress(normalizedDetail);
        cinema.setAddress(fullAddress);
    }
}
