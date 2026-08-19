package spring.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "username cant blank")
    @Email(message = "username must be a valid email")
    @Size(max = 100, message = "username must not exceed 100 characters")
    private String username;
    @NotBlank(message = "password cant blank")
    @Size(min = 8, max = 72, message = "password must contain between 8 and 72 characters")
    private String password;
}
