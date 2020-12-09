package com.pixeon.app.service;

import com.pixeon.app.exception.OutOfBudgetExeption;
import com.pixeon.app.model.Budget;
import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    @Transactional
    public boolean initializeBudget(HealthCareInstitution hci) {
        Optional<Budget> optionalBudget = budgetRepository.findByHealthCareInstitution(hci);
        if(optionalBudget.isPresent()){
            return false;
        }
        Budget budget = new Budget();
        budget.setHealthCareInstitution(hci);
        budgetRepository.save(budget);
        return true;
    }

    @Transactional
    public boolean spendBudget(HealthCareInstitution hci, Long amountToSpend) throws OutOfBudgetExeption{
        Optional<Budget> optionalBudget = budgetRepository.findByHealthCareInstitution(hci);
        if(optionalBudget.isPresent()){
            Budget budget = optionalBudget.get();
            if(budget.getAmount() - amountToSpend >= 0){
                budget.setAmount(budget.getAmount() - amountToSpend);
                budgetRepository.save(budget);
                return true;
            } else {
                throw new OutOfBudgetExeption();
            }
        }
        return false;
    }

}
