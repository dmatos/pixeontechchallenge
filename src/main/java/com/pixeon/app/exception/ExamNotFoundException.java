package com.pixeon.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExamNotFoundException extends  Exception{
    public ExamNotFoundException(Long id){
        super("Exam with ID "+id+" not found.");
    }
}
