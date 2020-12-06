package com.pixeon.app.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthCareInstitutionView {
    private String Name;
    private String CNPJ;

    public boolean isValid(){
        return !(this.Name == null || this.CNPJ == null);
    }
}
