package spring.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cinemas")
public class Cinema extends AbstractEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code", referencedColumnName = "code")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code", referencedColumnName = "code")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_code", referencedColumnName = "code")
    private Ward ward;

    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    @Column(nullable = false, length = 500)
    private String address;

}
