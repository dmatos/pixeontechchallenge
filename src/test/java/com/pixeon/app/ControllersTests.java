package com.pixeon.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pixeon.app.exception.OutOfBudgetExeption;
import com.pixeon.app.view.ExamView;
import com.pixeon.app.view.HealthCareInstitutionView;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ControllersTests implements Runnable{

    public static final String healthCareUrl = "/health-care-institution";
    public static final String examUrl = "/exam";
    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(
                    MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    Charset.forName("utf8"));

    private static HealthCareInstitutionView hciView;

    @Autowired
    private MockMvc mvc;

    @Test
    public void createThenDeleteInstitution() throws Exception{

        String randomName = "InstitutionTest"+Math.random()*10000%10000;

        HealthCareInstitutionView view = new HealthCareInstitutionView(randomName, "33.766.920/0001-74");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String hciRequestJson = ow.writeValueAsString(view);

        log.info(hciRequestJson);

        this.mvc.perform(post(this.healthCareUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(hciRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Institution "+view.getName()+" - "+view.getCNPJ()+ " creation success."));

        this.mvc.perform(post(this.healthCareUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(hciRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Health Care Institution with CNPJ "+ view.getCNPJ() +" already exists"));


        ExamView examView = new ExamView();
        examView.setHealthCareInstitution(view);
        examView.setPatientName("John Doe");
        examView.setPatientAge(25);
        examView.setPatientGender("Male");
        examView.setPhysicianName("Dr. Brown");
        examView.setPhysicianCRM("123456");
        examView.setProcedureName("Appendectomy");

        String examRequestJson = ow.writeValueAsString(examView);

        this.mvc.perform(post(this.examUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(examRequestJson))
                .andExpect(status().isOk());

        //TODO check concurrence of exam creation requisitions as the budget runs out (Must use Thread!!!)

        //TODO retrieve exam

        //TODO modify exam

        //TODO delete exam

        //TODO list paid exams (with fields Id and Retrieved)

    }

    @Test
    @Timeout(value = 2, unit = MINUTES)
    public void createManyExamsSimultaneously() throws Exception{

            final int numOfThreads = 30;
            Thread[] threads = new Thread[numOfThreads];
            String randomName = "InstitutionTest" + Math.random() * 10000 % 10000;

            hciView = new HealthCareInstitutionView(randomName, "33.766.920/0001-74");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String hciRequestJson = ow.writeValueAsString(hciView);

            this.mvc.perform(post(this.healthCareUrl + "/create").contentType(APPLICATION_JSON_UTF8).content(hciRequestJson));

            for (int i = 0; i < numOfThreads; i++) {
                threads[i] = new Thread(this);
                threads[i].start();
            }

            for(int i = 0; i < numOfThreads; i++){
                threads[i].join();
            }

            ExamView examView = new ExamView();
            examView.setHealthCareInstitution(hciView);
            examView.setPatientName("John Doe");
            examView.setPatientAge(25);
            examView.setPatientGender("Male");
            examView.setPhysicianName("Dr. Brown");
            examView.setPhysicianCRM("123456");
            examView.setProcedureName("Appendectomy");

            String examRequestJson = ow.writeValueAsString(examView);
            this.mvc.perform(post(this.examUrl + "/create").contentType(APPLICATION_JSON_UTF8).content(examRequestJson))
                    .andExpect(status().isPaymentRequired());
    }

    @Override
    public void run(){
        try {
            ExamView examView = new ExamView();
            examView.setHealthCareInstitution(hciView);
            examView.setPatientName("John Doe");
            examView.setPatientAge(25);
            examView.setPatientGender("Male");
            examView.setPhysicianName("Dr. Brown");
            examView.setPhysicianCRM("123456");
            examView.setProcedureName("Appendectomy");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            String examRequestJson = ow.writeValueAsString(examView);
            this.mvc.perform(post(this.examUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(examRequestJson));

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
