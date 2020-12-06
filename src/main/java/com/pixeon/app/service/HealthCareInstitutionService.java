package com.pixeon.app.service;

import com.pixeon.app.exception.HCIExistsException;
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

    public List<HealthCareInstitution> findAll(){
        var resultList = (List<HealthCareInstitution>) repository.findAll();
        return resultList;
    }

    @Transactional
    public HealthCareInstitution createNewInstitution(HealthCareInstitutionView view) throws Exception{
        if(view.isValid()){
            Optional<HealthCareInstitution> hci = repository.findFirstByCNPJ(view.getCNPJ());
            if(hci.isPresent()){
                throw new HCIExistsException(view.getCNPJ());
            } else {
                if(CNPJValidator.isCNPJ(view.getCNPJ())){
                    HealthCareInstitution newHCI = new HealthCareInstitution();
                    newHCI.setName(view.getName());
                    newHCI.setCNPJ(view.getCNPJ());
                    repository.save(newHCI);
                    return newHCI;
                } else {
                    throw new InvalidCNPJException(view.getCNPJ());
                }
            }
        } else {
            throw new Exception("Name and/or CNPJ fields not present.");
        }
    }
}
