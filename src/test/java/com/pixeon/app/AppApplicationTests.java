package com.pixeon.app;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTests {

    public static final String healthCareUrl = "/health-care-institution";
    public static final String examUrl = "/exam";
    public static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(
                    MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    Charset.forName("utf8"));

    @Autowired
    protected MockMvc mvc;

    @Test
    public void validarInicializacao() throws Exception{
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string("Welcome to Pìxeon Tech Challenge!"));

    }

}
