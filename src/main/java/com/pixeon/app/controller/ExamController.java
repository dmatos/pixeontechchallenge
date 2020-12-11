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
import org.springframework.http.HttpStatus;
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

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public ExamView create(@RequestBody ExamView view)
            throws InvalidExamDataInputException, OutOfBudgetExeption, HCINotFoundException{
        Exam exam = examService.create(view);
        ExamView examView = this.modelToView(view.getHealthCareInstitution(), exam);
        return examView;
    }

    @GetMapping("{exam_id}")
    @ResponseStatus(HttpStatus.OK)
    public ExamView retrieve(
            @RequestBody HealthCareInstitutionView view,
            @PathVariable("exam_id") Long exam_id)
            throws HCINotFoundException, ExamNotFoundException, OutOfBudgetExeption{
        HealthCareInstitution hci = healthCareInstitutionService.findByNameAndCNPJ(view);
        Exam exam = examService.retrieve(hci, exam_id);
        ExamView examView = this.modelToView(view, exam);
        return examView;
    }

    @DeleteMapping("{exam_id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @RequestBody HealthCareInstitutionView view,
            @PathVariable("exam_id") Long exam_id)
            throws ExamNotFoundException, HCINotFoundException{
        HealthCareInstitution hci = healthCareInstitutionService.findByNameAndCNPJ(view);
        examService.delete(hci, exam_id);
    }

    @PostMapping("{exam_id}/update")
    @ResponseStatus(HttpStatus.OK)
    public ExamView update(
            @RequestBody ExamView view,
            @PathVariable("exam_id") Long exam_id)
            throws HCINotFoundException, ExamNotFoundException{
        HealthCareInstitution hci = healthCareInstitutionService.findByNameAndCNPJ(view.getHealthCareInstitution());
        return modelToView(view.getHealthCareInstitution(), examService.update(hci, exam_id, view));
    }

    protected ExamView modelToView(HealthCareInstitutionView hciView, Exam exam){
        ExamView examView = new ExamView();
        examView.setId(exam.getId());
        examView.setHealthCareInstitution(hciView);
        examView.setPatientName(exam.getPatientName());
        examView.setPatientAge(exam.getPatientAge());
        examView.setPatientGender(exam.getPatientGender());
        examView.setPhysicianName(exam.getPhysicianName());
        examView.setPhysicianCRM(exam.getPhysicianCRM());
        examView.setProcedureName(exam.getProcedureName());
        examView.setVersion(exam.getVersion());
        return examView;
    }
}
