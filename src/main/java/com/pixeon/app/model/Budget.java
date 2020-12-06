package com.pixeon.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "budget")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "health_care_institution_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL)
    private HealthCareInstitution healthCareInstitution;

    private Long amount;

    @PrePersist
    public void prePersist(){
        this.amount = 20L;
    }
}
