package spring.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCinemaRequest {

    @NotBlank(message = "Cinema name must not be blank")
    @Size(max = 150, message = "Cinema name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Province code must not be blank")
    @Size(max = 20, message = "Province code must not exceed 20 characters")
    private String provinceCode;

    @NotBlank(message = "District code must not be blank")
    @Size(max = 20, message = "District code must not exceed 20 characters")
    private String districtCode;

    @NotBlank(message = "Ward code must not be blank")
    @Size(max = 20, message = "Ward code must not exceed 20 characters")
    private String wardCode;

    @NotBlank(message = "Detail address must not be blank")
    @Size(max = 255, message = "Detail address must not exceed 255 characters")
    private String detailAddress;
}
