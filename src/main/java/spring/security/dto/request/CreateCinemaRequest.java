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

    @NotBlank(message = "Cinema address must not be blank")
    @Size(max = 500, message = "Cinema address must not exceed 500 characters")
    private String address;
}
