package com.pixeon.app.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidCNPJException extends Exception{
    public InvalidCNPJException(String cnpj){
        super("Invalid CNPJ ("+cnpj+").");
    }
}
