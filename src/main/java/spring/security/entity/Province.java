package spring.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "provinces")
public class Province {

    @Id
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "unit_type", length = 50)
    private String unitType;
}
