package com.pixeon.app.controller;

import com.pixeon.app.exception.HCIAlreadyExistsException;
import com.pixeon.app.exception.InvalidCNPJException;
import com.pixeon.app.exception.InvalidHCIDataInputException;
import com.pixeon.app.service.HealthCareInstitutionService;
import com.pixeon.app.view.HealthCareInstitutionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/health-care-institution")
public class HealthCareInstitutionController {

    @Autowired
    private HealthCareInstitutionService service;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public HealthCareInstitutionView create(@RequestBody HealthCareInstitutionView view)
            throws InvalidCNPJException, InvalidHCIDataInputException, HCIAlreadyExistsException {
        if(service.createNewInstitution(view))
            return view;
        throw new InvalidHCIDataInputException();
    }
}
