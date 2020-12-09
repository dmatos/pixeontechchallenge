package com.pixeon.app.view;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthCareInstitutionView {

    @NotNull
    private String name;

    @NotNull
    private String CNPJ;

    public String getCNPJ(){
        return this.CNPJ.replaceAll("[^\\d]", "");
    }
}
