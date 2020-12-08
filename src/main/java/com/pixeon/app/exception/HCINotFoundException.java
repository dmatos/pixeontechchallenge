package com.pixeon.app.exception;

public class HCINotFoundException extends Exception{
    public HCINotFoundException(String name, String CNPJ){
        super("Health Care Institution with name "+name+" and CNPJ "+CNPJ+" not found");
    }
}
