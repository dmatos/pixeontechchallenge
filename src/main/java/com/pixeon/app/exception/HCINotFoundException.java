package com.pixeon.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HCINotFoundException extends Exception{
    public HCINotFoundException(String name, String CNPJ){
        super("Health Care Institution with name "+name+" and CNPJ "+CNPJ+" not found");
    }
}
