package com.pixeon.app.view;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamView {

    private Long id = 0L;

    private Long version = 0L;

    @NotNull
    private HealthCareInstitutionView healthCareInstitution;

    private String patientName;

    private Integer patientAge;

    private String patientGender;

    private String physicianName;

    private String physicianCRM;

    private String procedureName;

}
