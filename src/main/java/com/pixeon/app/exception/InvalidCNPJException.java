package com.pixeon.app.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCNPJException extends Exception{
    public InvalidCNPJException(String cnpj){
        super("Invalid CNPJ ("+cnpj+").");
    }
}
