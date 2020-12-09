package com.pixeon.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_care_institution",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "cnpj"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthCareInstitution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cnpj", nullable = false)
    private String CNPJ;

    @Column(nullable = false)
    private LocalDateTime dataInclusaoRegistro;

    @PrePersist
    public void prePersist(){
        this.dataInclusaoRegistro = LocalDateTime.now();
        this.CNPJ = this.CNPJ.replaceAll("[^\\d]", "");
    }

}
