package com.pixeon.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "health_care_institution", schema = "pixeon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthCareInstitutionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //TODO nome não pode ser vazio
    private String name;

    //TODO deve-se validar CNPJ na criação
    private String CNPJ;
}
