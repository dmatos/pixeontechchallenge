package com.pixeon.app.service;

import com.pixeon.app.exception.HCIExistsException;
import com.pixeon.app.exception.HCINotFoundException;
import com.pixeon.app.exception.InvalidCNPJException;
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

    public List<HealthCareInstitution> findAll(){
        var resultList = (List<HealthCareInstitution>) repository.findAll();
        return resultList;
    }

    public HealthCareInstitution findByNameAndCNPJ(HealthCareInstitutionView view) throws HCINotFoundException {
        Optional<HealthCareInstitution> optionalHCI = repository.findFirstByCNPJAndName(view.getCNPJ(), view.getName());
        if(optionalHCI.isPresent()){
            return optionalHCI.get();
        }
        return null;
    }

    @Transactional
    public boolean createNewInstitution(HealthCareInstitutionView view) throws Exception{
        if(view.isValid()){
            Optional<HealthCareInstitution> optionalHCI = repository.findFirstByCNPJAndName(view.getCNPJ(), view.getName());
            if(optionalHCI.isPresent()){
                throw new HCIExistsException(view.getCNPJ());
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
        } else {
            throw new Exception("Name and/or CNPJ fields not present.");
        }
    }

    @Transactional
    public boolean deleteInstitution(HealthCareInstitutionView view) throws Exception{
        if(view.isValid()){
            Optional<HealthCareInstitution> optionalHCI = repository.findFirstByCNPJAndName(view.getCNPJ(), view.getName());
            if(optionalHCI.isPresent()){
                budgetService.deleteBudget(optionalHCI.get());
                repository.deleteByCNPJ(optionalHCI.get().getCNPJ());
                return true;
            }
            return false;
        } else {
            throw new Exception("Name and/or CNPJ fields not present.");
        }
    }
}
