package com.pixeon.app.exception;

public class ExamNotFoundException extends  Exception{
    public ExamNotFoundException(Long id){
        super("Exam with ID "+id+" not found.");
    }
}
