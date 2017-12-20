package com.vlad.demo.endpoint;

import com.vlad.demo.task.service.exception.TaskNotFoundException;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorData> handleTaskNotFoundException(TaskNotFoundException e){
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new ErrorData(e.getMessage(), NOT_FOUND.value()));
    }
}
