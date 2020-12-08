package com.pixeon.app.service;

import com.pixeon.app.exception.ExamNotFoundException;
import com.pixeon.app.exception.HCINotFoundException;
import com.pixeon.app.exception.InvalidExamDataInputException;
import com.pixeon.app.exception.OutOfBudgetExeption;
import com.pixeon.app.model.Budget;
import com.pixeon.app.model.Exam;
import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.repository.ExamRepository;
import com.pixeon.app.view.ExamView;
import com.pixeon.app.view.HealthCareInstitutionView;
import io.micrometer.core.instrument.config.validate.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@Service
public class ExamService {

    private Long EXAM_CREATION_COST = 1L;
    private Long EXAM_RETRIEVAL_COST = 1L;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    BudgetService budgetService;

    @Autowired
    HealthCareInstitutionService healthCareInstitutionService;

    @Transactional
    public Exam create(ExamView view)
            throws InvalidExamDataInputException, OutOfBudgetExeption, HCINotFoundException {
        if(view.isValid()){
            HealthCareInstitution hci = healthCareInstitutionService.findByNameAndCNPJ(view.getHealthCareInstitution());
            Exam exam = new Exam();
            exam.setHealthCareInstitution(hci);
            exam.setPatientName(view.getPatientName());
            exam.setPatientAge(view.getPatientAge());
            exam.setPatientGender(view.getPatientGender());
            exam.setPhysicianName(view.getPhysicianName());
            exam.setPhysicianCRM(view.getPhysicianCRM());
            exam.setProcedureName(view.getProcedureName());
            exam = examRepository.save(exam);
            if (budgetService.spendBudget(hci, EXAM_CREATION_COST)) {
                exam.setPaid(true);
                return examRepository.save(exam);
            }
            throw new OutOfBudgetExeption();
        }
        throw new InvalidExamDataInputException();
    }

    @Transactional
    public Exam retrieve(HealthCareInstitution hci, Long exam_id) throws ExamNotFoundException{
        try {
            Optional<Exam> optionalExam = examRepository.findFirstById(exam_id);
            if(optionalExam.isPresent()){
                Exam exam = optionalExam.get();
                if(exam.getPaid()){
                    if(!exam.getAlreadyRetrieved()){
                        if(exam.getHealthCareInstitution().getId() == hci.getId()){
                            if (budgetService.spendBudget(hci, EXAM_RETRIEVAL_COST)) {
                                exam.setAlreadyRetrieved(true);
                                return examRepository.save(exam);
                            }
                            throw new OutOfBudgetExeption();
                        }
                    } else {
                        return exam;
                    }
                }
                throw new ExamNotFoundException(exam_id);
            }
            throw new ExamNotFoundException(exam_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ExamNotFoundException(exam_id);
    }
}
