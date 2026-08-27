package spring.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.request.CreateRoomRequest;
import spring.security.dto.request.UpdateRoomRequest;
import spring.security.dto.response.RoomResponse;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.RoomService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminRoomControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RoomService roomService;

    @InjectMocks
    private AdminRoomController adminRoomController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminRoomController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void createRoom_Success_Returns201() throws Exception {
        Long cinemaId = 1L;
        CreateRoomRequest request = CreateRoomRequest.builder().name("Room 01").build();
        RoomResponse response = RoomResponse.builder().id(10L).name("Room 01").cinemaId(cinemaId).build();

        when(roomService.createRoom(eq(cinemaId), any(CreateRoomRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/cinemas/{cinemaId}/rooms", cinemaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.name").value("Room 01"));

        verify(roomService).createRoom(eq(cinemaId), any(CreateRoomRequest.class));
    }

    @Test
    void createRoom_InvalidName_Returns400() throws Exception {
        Long cinemaId = 1L;
        CreateRoomRequest request = CreateRoomRequest.builder().name("").build();

        mockMvc.perform(post("/api/admin/cinemas/{cinemaId}/rooms", cinemaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void updateRoom_Success_Returns200() throws Exception {
        Long roomId = 10L;
        UpdateRoomRequest request = UpdateRoomRequest.builder().name("Room 01 Gold").build();
        RoomResponse response = RoomResponse.builder().id(roomId).name("Room 01 Gold").build();

        when(roomService.updateRoom(eq(roomId), any(UpdateRoomRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/admin/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Room 01 Gold"));
    }

    @Test
    void deleteRoom_Success_Returns204() throws Exception {
        Long roomId = 10L;
        doNothing().when(roomService).deleteRoom(roomId);

        mockMvc.perform(delete("/api/admin/rooms/{id}", roomId))
                .andExpect(status().isNoContent());

        verify(roomService).deleteRoom(roomId);
    }

    @Test
    void deleteRoom_HasSeats_Returns409() throws Exception {
        Long roomId = 10L;
        doThrow(new AppException(ErrorCode.ROOM_HAS_SEATS))
                .when(roomService).deleteRoom(roomId);

        mockMvc.perform(delete("/api/admin/rooms/{id}", roomId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.ROOM_HAS_SEATS.getCode()));
    }
}
