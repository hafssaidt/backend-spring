package org.idtaleb.skylin.controllers;

import org.idtaleb.skylin.entities.*;
import org.idtaleb.skylin.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIndependentTasks(Principal principal, @RequestParam boolean completed) {
        try {
            List<TaskApp> tasks = taskService.getIndependentTasks(principal.getName(), completed);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/project/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTasksInProject(@PathVariable String projectId, @RequestParam boolean completed) {
        try {
            List<TaskApp> tasks = taskService.getTasksInProject(projectId, completed);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createIndependentTask(Principal principal, @RequestBody TaskApp task) {
        try {
            TaskApp taskCreated = taskService.createIndependentTask(principal.getName(), task);
            return new ResponseEntity<>(taskCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/kanban/{kanbanItemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTaskInKanbanItem(Principal principal, @PathVariable String kanbanItemId, @RequestBody TaskApp task) {
        try {
            TaskApp taskCreated = taskService.createTaskInKanbanItem(principal.getName(), kanbanItemId, task);
            return new ResponseEntity<>(taskCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/project/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTaskInProject(Principal principal, @PathVariable String projectId, @RequestBody TaskApp task) {
        try {
            TaskApp taskCreated = taskService.createTaskInProject(principal.getName(), projectId, task);
            return new ResponseEntity<>(taskCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTask(@PathVariable String taskId) {
        try {
            TaskApp task = taskService.getTask(taskId);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTask(@PathVariable String taskId, @RequestBody TaskApp task) {
        try {
            TaskApp taskUpdated = taskService.updateTask(taskId, task);
            return new ResponseEntity<>(taskUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTaskName(@PathVariable String taskId, @RequestParam String name) {
        try {
            TaskApp taskUpdated = taskService.updateTaskName(taskId, name);
            return new ResponseEntity<>(taskUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateIndependentTaskStatus(Principal principal, @PathVariable String taskId, @RequestParam boolean completed) {
        try {
            TaskUpdateManager taskUpdateManager = taskService.updateIndependentTaskStatus(principal.getName(), taskId, completed);
            return new ResponseEntity<>(taskUpdateManager, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/project/{projectId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTaskStatusInProject(@PathVariable String taskId, @PathVariable String projectId, @RequestParam boolean completed) {
        try {
            TaskUpdateManager taskUpdateManager = taskService.updateTaskStatusInProject(projectId, taskId, completed);
            return new ResponseEntity<>(taskUpdateManager, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTaskOrder(Principal principal, @PathVariable String taskId, @RequestParam int newOrder) {
        try {
            List<TaskApp> tasksUpdated = taskService.updateIndependentTaskOrder(principal.getName(), taskId, newOrder);
            return new ResponseEntity<>(tasksUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/kanban/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTaskOrderInKanbanItem(@PathVariable String taskId, @RequestParam int newOrder) {
        try {
            List<TaskApp> tasksUpdated = taskService.updateTaskOrderInKanbanItem(taskId, newOrder);
            return new ResponseEntity<>(tasksUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/project/{projectId}/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTaskOrderInProject(@PathVariable String taskId, @PathVariable String projectId, @RequestParam int newOrder) {
        try {
            List<TaskApp> tasksUpdated = taskService.updateTaskOrderInProject(projectId, taskId, newOrder);
            return new ResponseEntity<>(tasksUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{taskId}/kanban/{newKanbanItemId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> moveTaskToAnotherKanbanItem(@PathVariable String taskId, @PathVariable String newKanbanItemId, @RequestParam int newOrder) {
        try {
            KanbanUpdateManager kanbanUpdateManager = taskService.moveTaskToAnotherKanbanItem(newKanbanItemId, taskId, newOrder);
            return new ResponseEntity<>(kanbanUpdateManager, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(Principal principal, @PathVariable String taskId) {
        try {
            List<TaskApp> tasks = taskService.deleteIndependentTask(principal.getName(), taskId);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}/kanban")
    public ResponseEntity<?> deleteTaskInKanbanItem(@PathVariable String taskId) {
        try {
            List<TaskApp> tasks = taskService.deleteTaskInKanbanItem(taskId);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}/project/{projectId}")
    public ResponseEntity<?> deleteTaskInProject(@PathVariable String taskId, @PathVariable String projectId) {
        try {
            List<TaskApp> tasks = taskService.deleteTaskInProject(projectId, taskId);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/completed")
    public ResponseEntity<?> deleteIndependentCompletedTasks(Principal principal) {
        try {
            taskService.deleteIndependentCompletedTasks(principal.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("project/{projectId}/completed")
    public ResponseEntity<?> deleteCompletedTasksInProject(@PathVariable String projectId) {
        try {
            taskService.deleteCompletedTasksInProject(projectId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllIndependentTasks(Principal principal) {
        try {
            taskService.deleteAllIndependentTasks(principal.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("project/{projectId}")
    public ResponseEntity<?> deleteAllTasksInProject(@PathVariable String projectId) {
        try {
            taskService.deleteAllTasksInProject(projectId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countTasks(Principal principal) {
        try {
            CountTask countTask = taskService.countTasks(principal.getName());
            return new ResponseEntity<>(countTask, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "project/progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countProgressTasksProject(Principal principal) {
        try {
            List<ProgressProject> progressProjectList = taskService.countProgressTasksProject(principal.getName());
            return new ResponseEntity<>(progressProjectList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "kanban/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countTasksInKanbanItems(Principal principal) {
        try {
            List<CountTasksInKanbanItem> countTasksInKanbanItemList = taskService.countTasksInKanbanItems(principal.getName());
            return new ResponseEntity<>(countTasksInKanbanItemList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "count/date", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countTasksByDate(Principal principal) {
        try {
            List<CountTasksDate> countTasksDateList = taskService.countTasksByDate(principal.getName());
            return new ResponseEntity<>(countTasksDateList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}

