package com.pixeon.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HCIAlreadyExistsException extends Exception{
    public HCIAlreadyExistsException(String cnpj){
        super("Health Care Institution with CNPJ "+ cnpj +" already exists");
    }
}
