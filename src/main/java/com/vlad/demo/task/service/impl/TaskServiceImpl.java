package com.vlad.demo.task.service.impl;

import com.vlad.demo.task.service.TaskService;
import com.vlad.demo.task.service.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskServiceImpl implements TaskService {


    @Override
    public Task findTaskById(String taskId)  {
       throw new IllegalStateException("Just for fun and try out.");
    }
}
