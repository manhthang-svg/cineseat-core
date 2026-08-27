package spring.security.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.security.enums.SeatType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatRequest {

    @NotBlank(message = "Row label must not be blank")
    @Size(max = 10, message = "Row label must not exceed 10 characters")
    private String rowLabel;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    private Integer seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;
}
