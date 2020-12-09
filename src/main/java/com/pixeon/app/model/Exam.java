package com.pixeon.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "health_care_institution_id", referencedColumnName = "id")
    @OneToOne
    private HealthCareInstitution healthCareInstitution;

    private String patientName;

    private Integer patientAge;

    private String patientGender;

    private String physicianName;

    private String physicianCRM;

    private String procedureName;

    private Boolean alreadyRetrieved;

    private Boolean paid;

    @Column(nullable = false)
    private LocalDateTime dateOfInclusion;

    private LocalDateTime dateOfLastUpdate;

    @Version
    private Long version;

    @PrePersist
    public void prePersist(){
        this.dateOfInclusion = LocalDateTime.now();
        this.alreadyRetrieved = false;
        this.paid = false;
    }

    @PreUpdate
    public void preUpdate(){
        this.setDateOfLastUpdate(LocalDateTime.now());
    }
}
