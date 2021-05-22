package com.vlad.demo.task.service;

import com.vlad.demo.task.service.model.Task;

public interface TaskService {

   Task findTaskById(String taskId);
}
