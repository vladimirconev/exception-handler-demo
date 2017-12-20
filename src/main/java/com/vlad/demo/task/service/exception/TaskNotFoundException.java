package com.vlad.demo.task.service.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Exception e) {
        super(e);
    }

    public TaskNotFoundException(String msg) {
        super(msg);
    }

}
