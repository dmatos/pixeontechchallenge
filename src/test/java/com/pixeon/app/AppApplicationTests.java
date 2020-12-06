package com.pixeon.app;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void validarInicializacao() throws Exception{
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string("Welcome to Pìxeon Tech Challenge!"));

    }

}
