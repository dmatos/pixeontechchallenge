package com.pixeon.app.service;

import com.pixeon.app.exception.HCIAlreadyExistsException;
import com.pixeon.app.exception.HCINotFoundException;
import com.pixeon.app.exception.InvalidCNPJException;
import com.pixeon.app.exception.InvalidHCIDataInputException;
import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.repository.HealthCareInstitutionRepository;
import com.pixeon.app.util.CNPJValidator;
import com.pixeon.app.view.HealthCareInstitutionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HealthCareInstitutionService {

    @Autowired
    private HealthCareInstitutionRepository repository;

    @Autowired
    private BudgetService budgetService;

    public HealthCareInstitution findByNameAndCNPJ(HealthCareInstitutionView view)
            throws HCINotFoundException {
        Optional<HealthCareInstitution> optionalHCI = repository.findFirstByCNPJAndName(view.getCNPJ(), view.getName());
        if(optionalHCI.isPresent()){
            return optionalHCI.get();
        }
        throw new HCINotFoundException(view.getName(), view.getCNPJ());
    }

    @Transactional
    public boolean createNewInstitution(HealthCareInstitutionView view)
            throws InvalidCNPJException, InvalidHCIDataInputException, HCIAlreadyExistsException{
        Optional<HealthCareInstitution> optionalHCI = repository.findFirstByCNPJAndName(view.getCNPJ(), view.getName());
        if(optionalHCI.isPresent()){
            throw new HCIAlreadyExistsException(view.getCNPJ());
        } else {
            if(CNPJValidator.isCNPJ(view.getCNPJ())){
                HealthCareInstitution newHCI = new HealthCareInstitution();
                newHCI.setName(view.getName());
                newHCI.setCNPJ(view.getCNPJ());
                newHCI = repository.save(newHCI);
                budgetService.initializeBudget(newHCI);
                return true;
            } else {
                throw new InvalidCNPJException(view.getCNPJ());
            }
        }

    }
}
