package com.pixeon.app.service;

import com.pixeon.app.exception.ExamNotFoundException;
import com.pixeon.app.exception.HCINotFoundException;
import com.pixeon.app.exception.InvalidExamDataInputException;
import com.pixeon.app.exception.OutOfBudgetExeption;
import com.pixeon.app.model.Exam;
import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.repository.ExamRepository;
import com.pixeon.app.view.ExamView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
@Service
public class ExamService {

    // hardcoded :( In a real world application it should be retrieved from a database
    private Long EXAM_CREATION_COST = 1L;
    private Long EXAM_RETRIEVAL_COST = 1L;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    BudgetService budgetService;

    @Autowired
    HealthCareInstitutionService healthCareInstitutionService;

    protected boolean viewValidationForCreation(ExamView view){
            return view.getHealthCareInstitution() != null
                    && view.getPatientName() != null
                    && view.getPatientGender() != null
                    && view.getPhysicianName() != null
                    && view.getPhysicianCRM() != null
                    && view.getProcedureName() != null;
    }

    @Transactional
    public Exam create(ExamView view)
            throws InvalidExamDataInputException, OutOfBudgetExeption, HCINotFoundException {
        if(this.viewValidationForCreation(view)){
            HealthCareInstitution hci = healthCareInstitutionService.findByNameAndCNPJ(view.getHealthCareInstitution());
            Exam exam = this.viewToModel(hci, view, null);
            exam = examRepository.save(exam);
            if (budgetService.spendBudget(hci, EXAM_CREATION_COST)) {
                exam.setPaid(true);
                return examRepository.save(exam);
            }
        }
        throw new InvalidExamDataInputException();
    }

    @Transactional
    public Exam retrieve(HealthCareInstitution hci, Long exam_id)
            throws ExamNotFoundException, OutOfBudgetExeption{
        Optional<Exam> optionalExam = examRepository.findFirstByIdAndHealthCareInstitution(exam_id, hci);
        if(optionalExam.isPresent()){
            Exam exam = optionalExam.get();
            if(exam.getPaid()){
                if(!exam.getAlreadyRetrieved()){
                    if (budgetService.spendBudget(hci, EXAM_RETRIEVAL_COST)) {
                        exam.setAlreadyRetrieved(true);
                        return examRepository.save(exam);
                    }
                }
                else return exam;
            }
            else throw new ExamNotFoundException(exam_id);
        }
        throw new ExamNotFoundException(exam_id);
    }

    @Transactional
    public void delete(HealthCareInstitution hci, Long exam_id)
            throws ExamNotFoundException{
        Optional<Exam> optionalExam = examRepository.findFirstByIdAndHealthCareInstitution(exam_id, hci);
        if(optionalExam.isPresent()){
            examRepository.deleteById(exam_id);
        }
        else throw new ExamNotFoundException(exam_id);
    }

    @Transactional
    public Exam update(HealthCareInstitution hci, Long exam_id, ExamView view)
            throws ExamNotFoundException{
        Optional<Exam> optionalExam = examRepository.findFirstByIdAndHealthCareInstitution(exam_id, hci);
        if(optionalExam.isPresent()){
            Exam exam = this.viewToModel(hci, view, optionalExam.get());
            return examRepository.save(exam);
        }
        else throw new ExamNotFoundException(exam_id);
    }

    protected Exam viewToModel(HealthCareInstitution hci, ExamView view, Exam exam){
        if(exam == null)
            exam = new Exam();
        exam.setHealthCareInstitution(hci);
        exam.setPatientName(view.getPatientName());
        exam.setPatientAge(view.getPatientAge());
        exam.setPatientGender(view.getPatientGender());
        exam.setPhysicianName(view.getPhysicianName());
        exam.setPhysicianCRM(view.getPhysicianCRM());
        exam.setProcedureName(view.getProcedureName());
        exam.setVersion(view.getVersion());
        return exam;
    }
}
