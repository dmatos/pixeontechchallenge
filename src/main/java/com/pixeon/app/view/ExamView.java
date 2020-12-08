package com.pixeon.app.view;

import com.pixeon.app.model.HealthCareInstitution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamView {

    private HealthCareInstitutionView healthCareInstitution;

    private String patientName;

    private Integer patientAge;

    private String patientGender;

    private String physicianName;

    private String physicianCRM;

    private String procedureName;

    public boolean isValid(){
        try {
            boolean valid = true;
            for (Field f : getClass().getDeclaredFields())
                valid = valid && (f.get(this) != null);
            return valid;
        }catch(IllegalAccessException ex){
            return false;
        }
    }
}
