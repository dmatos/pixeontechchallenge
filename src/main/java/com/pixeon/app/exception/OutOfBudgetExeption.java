package com.pixeon.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class OutOfBudgetExeption extends Exception{
    public OutOfBudgetExeption(){
        super("Running out of budget. :(");
    }
}
