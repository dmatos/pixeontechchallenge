package com.pixeon.app.exception;

import lombok.AllArgsConstructor;

public class HCIExistsException extends Exception{
    public HCIExistsException(String cnpj){
        super("Health Care Institution with CNPJ "+ cnpj +" already exists");
    }
}
