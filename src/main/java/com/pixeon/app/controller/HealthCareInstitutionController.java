package com.pixeon.app.controller;

import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.service.HealthCareInstitutionService;
import com.pixeon.app.view.HealthCareInstitutionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

@RestController
@RequestMapping("/health-care-institution")
public class HealthCareInstitutionController {

    @Autowired
    private HealthCareInstitutionService service;

    @PostMapping("create")
    public String create(@RequestBody HealthCareInstitutionView view){
        try {
            service.createNewInstitution(view);
            return "Institution " + view.getName() + " - " + view.getCNPJ() + " creation success.";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @GetMapping("list")
    public List<HealthCareInstitution> list(){
        return service.findAll();
    }

}
