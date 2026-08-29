package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WardResponse {
    private String code;
    private String name;
    private String fullName;
    private String unitType;
    private String districtCode;
}
