package org.idtaleb.skylin.controllers;

import org.idtaleb.skylin.entities.*;
import org.idtaleb.skylin.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    ProjectService projectService;
    @Autowired
    KanbanItemService kanbanItemService;
    @Autowired
    SubTaskService subTaskService;


    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserApp user) {
        try {
            UserApp userCreated = userService.createUser(user);
            String userId = userCreated.getId();

            taskService.createIndependentTask(userId, new TaskApp("Code review session", LocalDate.now()));

            projectService.createProject(userId, new Project("Tasks tracker app", Priority.HIGH, LocalDate.now()));
            projectService.createProject(userId, new Project("Deadline management", Priority.MEDIUM, LocalDate.now()));

            KanbanItem kanbanItemCreated = kanbanItemService.createKanbanItem(userId, new KanbanItem("To Do"));
            kanbanItemService.createKanbanItem(userId, new KanbanItem("In Progress"));
            kanbanItemService.createKanbanItem(userId, new KanbanItem("Done"));

            TaskApp taskCreated = taskService.createTaskInKanbanItem(userId, kanbanItemCreated.getId(), new TaskApp("Tasks management", LocalDate.now()));
            subTaskService.createSubTask(userId, taskCreated.getId(), new SubTask("subTask 1", false));
            subTaskService.createSubTask(userId, taskCreated.getId(), new SubTask("subTask 2", true));

            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(Principal principal) {
        try {
            UserApp user = userService.getUserById(principal.getName());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(Principal principal, @RequestBody UserApp user) {
        try {
            UserApp userUpdated = userService.updateUser(principal.getName(), user);
            return new ResponseEntity<>(userUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(Principal principal) {
        try {
            userService.deleteUser(principal.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
