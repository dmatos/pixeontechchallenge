package com.pixeon.app.controller;

import com.pixeon.app.exception.ExamNotFoundException;
import com.pixeon.app.exception.HCINotFoundException;
import com.pixeon.app.exception.InvalidExamDataInputException;
import com.pixeon.app.exception.OutOfBudgetExeption;
import com.pixeon.app.model.Budget;
import com.pixeon.app.model.Exam;
import com.pixeon.app.model.HealthCareInstitution;
import com.pixeon.app.repository.ExamRepository;
import com.pixeon.app.service.BudgetService;
import com.pixeon.app.service.ExamService;
import com.pixeon.app.service.HealthCareInstitutionService;
import com.pixeon.app.view.ExamView;
import com.pixeon.app.view.HealthCareInstitutionView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/exam")
public class ExamController {



    @Autowired
    ExamService examService;

    @Autowired
    HealthCareInstitutionService healthCareInstitutionService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private BudgetService budgetService;
    ;

    @PostMapping("create")
    public Long create(@RequestBody ExamView view) throws InvalidExamDataInputException, OutOfBudgetExeption, HCINotFoundException{
        return examService.create(view).getId();
    }

    @GetMapping("{exam_id}")
    public ExamView retrieve(
            @PathVariable("exam_id") Long exam_id,
            @RequestBody HealthCareInstitutionView view)
            throws HCINotFoundException, ExamNotFoundException, OutOfBudgetExeption{
        if (view.isValid()) {
                HealthCareInstitution hci = healthCareInstitutionService.findByNameAndCNPJ(view);
                Exam exam = examService.retrieve(hci, exam_id);
                ExamView examView = new ExamView();
                examView.setHealthCareInstitution(view);
                examView.setPatientName(exam.getPatientName());
                examView.setPatientAge(exam.getPatientAge());
                examView.setPatientGender(exam.getPatientGender());
                examView.setPhysicianName(exam.getPhysicianName());
                examView.setPhysicianCRM(exam.getPhysicianCRM());
                examView.setProcedureName(exam.getProcedureName());
        } else {
            throw new HCINotFoundException(view.getName(), view.getCNPJ());
        }
        throw new ExamNotFoundException(exam_id);
    }
}
