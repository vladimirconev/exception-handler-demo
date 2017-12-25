package com.vlad.demo.endpoint.v1;

import com.vlad.demo.task.service.TaskService;
import com.vlad.demo.task.service.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping(value = "/v1/task")
public class GetTaskController {

    private final TaskService taskService;

    @Autowired
    public GetTaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @RequestMapping(value = "/{taskId}", method = GET)
    public ResponseEntity getTask(@PathVariable  String taskId){
        final Task task = taskService.findTaskById(taskId);
        return ResponseEntity
                .status(OK)
                .body(task);
    }
}
