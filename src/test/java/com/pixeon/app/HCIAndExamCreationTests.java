package com.pixeon.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pixeon.app.view.ExamView;
import com.pixeon.app.view.HealthCareInstitutionView;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
public class HCIAndExamCreationTests extends AppApplicationTests implements Runnable{

    private static HealthCareInstitutionView hciView;

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
                .andExpect(status().isCreated());

        this.mvc.perform(post(this.healthCareUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(hciRequestJson))
                .andExpect(status().isBadRequest());


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
                .andExpect(status().isCreated());
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
                .andExpect(status().isBadRequest());
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
