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
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.CinemaMapper;
import spring.security.repository.CinemaRepository;
import spring.security.repository.RoomRepository;

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
    private CinemaMapper cinemaMapper;

    private CinemaServiceImpl cinemaService;

    @BeforeEach
    void setUp() {
        cinemaService = new CinemaServiceImpl(cinemaRepository, roomRepository, cinemaMapper);
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
                .address("  123 Nguyen Hue  ")
                .build();

        Cinema entity = new Cinema();
        Cinema saved = new Cinema();
        saved.setId(1L);
        saved.setName("CineVault Landmark");
        CinemaResponse response = CinemaResponse.builder().id(1L).name("CineVault Landmark").build();

        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse("CineVault Landmark")).thenReturn(false);
        when(cinemaMapper.toEntity(request)).thenReturn(entity);
        when(cinemaRepository.save(entity)).thenReturn(saved);
        when(cinemaMapper.toResponse(saved)).thenReturn(response);

        CinemaResponse result = cinemaService.createCinema(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("CineVault Landmark");
        assertThat(entity.getName()).isEqualTo("CineVault Landmark");
        assertThat(entity.getAddress()).isEqualTo("123 Nguyen Hue");
        verify(cinemaRepository).save(entity);
    }

    @Test
    void createCinema_DuplicateName_ThrowsException() {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("CineVault Landmark")
                .address("123 Nguyen Hue")
                .build();

        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalse("CineVault Landmark")).thenReturn(true);

        assertThatThrownBy(() -> cinemaService.createCinema(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CINEMA_ALREADY_EXISTS);

        verify(cinemaRepository, never()).save(any());
    }

    @Test
    void updateCinema_Success() {
        Long cinemaId = 1L;
        UpdateCinemaRequest request = UpdateCinemaRequest.builder()
                .name("CineVault Landmark Updated")
                .address("456 Le Loi")
                .build();

        Cinema existing = new Cinema();
        existing.setId(cinemaId);
        Cinema saved = new Cinema();
        saved.setId(cinemaId);
        CinemaResponse response = CinemaResponse.builder().id(cinemaId).name("CineVault Landmark Updated").build();

        when(cinemaRepository.findByIdAndDeletedFalse(cinemaId)).thenReturn(Optional.of(existing));
        when(cinemaRepository.existsByNameIgnoreCaseAndDeletedFalseAndIdNot("CineVault Landmark Updated", cinemaId)).thenReturn(false);
        when(cinemaRepository.save(existing)).thenReturn(saved);
        when(cinemaMapper.toResponse(saved)).thenReturn(response);

        CinemaResponse result = cinemaService.updateCinema(cinemaId, request);

        assertThat(result).isNotNull();
        verify(cinemaMapper).updateEntityFromDto(request, existing);
        verify(cinemaRepository).save(existing);
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
