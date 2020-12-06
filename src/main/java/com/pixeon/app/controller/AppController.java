package com.pixeon.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @RequestMapping("/")
    public String index(){
        return "Welcome to Pìxeon Tech Challenge!";
    }
}
