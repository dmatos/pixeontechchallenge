package com.pixeon.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pixeon.app.view.ExamView;
import com.pixeon.app.view.HealthCareInstitutionView;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
public class ExamDeleteAndEditionTests extends AppApplicationTests implements Runnable{

    private static HealthCareInstitutionView hciView;

    @Test
    public void createThenDeleteInstitution() throws Exception{

        String randomName = "InstitutionTest"+Math.random()*10000%10000;

        hciView = new HealthCareInstitutionView(randomName, "33.766.920/0001-74");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String hciRequestJson = ow.writeValueAsString(hciView);

        log.info(hciRequestJson);

        this.mvc.perform(post(this.healthCareUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(hciRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Institution "+hciView.getName()+" - "+hciView.getCNPJ()+ " creation success."));

        ExamView examView = new ExamView();
        examView.setHealthCareInstitution(hciView);
        examView.setPatientName("John Doe");
        examView.setPatientAge(25);
        examView.setPatientGender("Male");
        examView.setPhysicianName("Dr. Brown");
        examView.setPhysicianCRM("123456");
        examView.setProcedureName("Appendectomy");

        String examRequestJson = ow.writeValueAsString(examView);

        MvcResult examResult = this.mvc.perform(post(this.examUrl+"/create").contentType(APPLICATION_JSON_UTF8).content(examRequestJson))
                .andExpect(status().isCreated()).andReturn();

        examView = mapper.readValue(examResult.getResponse().getContentAsString(), ExamView.class);

        examResult = this.mvc.perform(get(this.examUrl+"/"+examView.getId()).contentType(APPLICATION_JSON_UTF8).content(hciRequestJson))
                .andExpect(status().isOk()).andReturn();

        examView = mapper.readValue(examResult.getResponse().getContentAsString(), ExamView.class);

        examView.setPhysicianName("Dr. Pink");
        examRequestJson = ow.writeValueAsString(examView);
        examResult = this.mvc.perform(post(this.examUrl+"/"+examView.getId()+"/update").contentType(APPLICATION_JSON_UTF8).content(examRequestJson))
                .andExpect(status().isOk()).andReturn();

        examView = mapper.readValue(examResult.getResponse().getContentAsString(), ExamView.class);
        Assertions.assertThat(examView.getPhysicianName()).isEqualTo("Dr. Pink");

        examRequestJson = ow.writeValueAsString(examView);
        this.mvc.perform(delete(this.examUrl+"/"+examView.getId()+"/delete").contentType(APPLICATION_JSON_UTF8).content(examRequestJson))
                .andExpect(status().isOk());

    }

    @Override
    public void run(){

    }
}
