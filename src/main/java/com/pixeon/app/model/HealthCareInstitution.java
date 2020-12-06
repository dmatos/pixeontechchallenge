package com.pixeon.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "health_care_institution")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthCareInstitution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Name;

    private String CNPJ;

    @PrePersist
    public void prePersist(){
        this.CNPJ = this.CNPJ.replaceAll("[^\\d]", "");
    }

}
