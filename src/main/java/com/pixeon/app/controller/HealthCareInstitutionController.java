package com.pixeon.app.controller;

import com.pixeon.app.exception.HCIExistsException;
import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.service.HealthCareInstitutionService;
import com.pixeon.app.view.HealthCareInstitutionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/health-care-institution")
public class HealthCareInstitutionController {

    @Autowired
    private HealthCareInstitutionService service;

    @PostMapping("create")
    public String create(@RequestBody HealthCareInstitutionView view){
        try {
            if(service.createNewInstitution(view))
                return "Institution " + view.getName() + " - " + view.getCNPJ() + " creation success.";
            else return "Institution " + view.getName() + " - " + view.getCNPJ() + " creation failure.";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @DeleteMapping("delete")
    public Boolean delete(@RequestBody HealthCareInstitutionView view){
        try {
            return service.deleteInstitution(view);
        } catch(Exception e){
            log.debug(e.getMessage());
            return false;
        }
    }

    @GetMapping("list")
    public List<HealthCareInstitution> list(){
        return service.findAll();
    }

}
