package org.idtaleb.skyline.controllers;

import org.idtaleb.skyline.entities.SubTask;
import org.idtaleb.skyline.services.SubTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/subTasks")
public class SubTaskController {
    @Autowired
    SubTaskService subTaskService;

    @GetMapping(path = "/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSubTasks(@PathVariable String taskId) {
        try {
            List<SubTask> subTasks = subTaskService.getAllSubTasks(taskId);
            return new ResponseEntity<>(subTasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/task/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSubTask(Principal principal, @PathVariable String taskId, @RequestBody SubTask task) {
        try {
            SubTask subTaskCreated = subTaskService.createSubTask(principal.getName(), taskId, task);
            return new ResponseEntity<>(subTaskCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{subTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubTask(@PathVariable String subTaskId) {
        try {
            SubTask subTask = subTaskService.getSubTask(subTaskId);
            return new ResponseEntity<>(subTask, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping(path = "/{subTaskId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSubTaskName(@PathVariable String subTaskId, @RequestParam String name) {
        try {
            SubTask subTaskUpdated = subTaskService.updateSubTaskName(subTaskId, name);
            return new ResponseEntity<>(subTaskUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "{subTaskId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSubTaskStatus(@PathVariable String subTaskId, @RequestParam boolean completed) {
        try {
            SubTask subTaskUpdated = subTaskService.updateSubTaskStatus(subTaskId, completed);
            return new ResponseEntity<>(subTaskUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "{subTaskId}/task/{taskId}/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSubTaskOrder(@PathVariable String subTaskId, @PathVariable String taskId, @RequestParam int newOrder) {
        try {
            List<SubTask> tasksUpdated = subTaskService.updateSubTaskOrder(taskId, subTaskId, newOrder);
            return new ResponseEntity<>(tasksUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "{subTaskId}/task/{taskId}")
    public ResponseEntity<?> deleteSubTask(@PathVariable String subTaskId, @PathVariable String taskId) {
        try {
            List<SubTask> subTasks = subTaskService.deleteSubTask(taskId, subTaskId);
            return new ResponseEntity<>(subTasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
