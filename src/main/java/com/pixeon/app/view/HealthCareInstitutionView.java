package com.pixeon.app.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthCareInstitutionView {
    private String name;
    private String CNPJ;

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

    public String getCNPJ(){
        return this.CNPJ.replaceAll("[^\\d]", "");
    }
}
