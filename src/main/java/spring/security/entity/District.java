package spring.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "districts")
public class District {

    @Id
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "unit_type", length = 50)
    private String unitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code", nullable = false, referencedColumnName = "code")
    private Province province;
}
