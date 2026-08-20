package spring.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private Set<RoleResponse> roles;
}
