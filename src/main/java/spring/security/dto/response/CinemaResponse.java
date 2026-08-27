package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CinemaResponse {
    private Long id;
    private String name;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
}
