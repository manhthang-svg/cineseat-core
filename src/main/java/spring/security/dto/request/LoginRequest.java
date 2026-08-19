package spring.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username cant blank")
    @Email(message = "username must be a valid email")
    @Size(max = 100, message = "username must not exceed 100 characters")
    private String username;
    @NotBlank(message = "password cant blank")
    @Size(max = 72, message = "password must not exceed 72 characters")
    private String password;
}
