package org.idtaleb.skyline.controllers;

import org.idtaleb.skyline.entities.CountProjectsByPriority;
import org.idtaleb.skyline.entities.Project;
import org.idtaleb.skyline.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProjects(Principal principal) {
        try {
            List<Project> projects = projectService.getAllProjects(principal.getName());
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProject(Principal principal, @RequestBody Project project) {
        try {
            Project projectCreated = projectService.createProject(principal.getName(), project);
            return new ResponseEntity<>(projectCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProject(@PathVariable String projectId, @RequestBody Project project) {
        try {
            Project projectUpdated = projectService.updateProject(projectId, project);
            return new ResponseEntity<>(projectUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{projectId}/name", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProjectName(@PathVariable String projectId, @RequestParam String name) {
        try {
            Project projectUpdated = projectService.updateProjectName(projectId, name);
            return new ResponseEntity<>(projectUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{projectId}/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProjectOrder(Principal principal, @PathVariable String projectId, @RequestParam int newOrder) {
        try {
            List<Project> projectsUpdated = projectService.updateProjectOrder(principal.getName(), projectId, newOrder);
            return new ResponseEntity<>(projectsUpdated, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProject(Principal principal, @PathVariable String projectId) {
        try {
            List<Project> projectsUpdated = projectService.deleteProject(principal.getName(), projectId);
            return new ResponseEntity<>(projectsUpdated, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countProjects(Principal principal) {
        try {
            long countProjects = projectService.countProjects(principal.getName());
            return new ResponseEntity<>(countProjects, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "count/priority", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> countProjectsByPriority(Principal principal) {
        try {
            List<CountProjectsByPriority> countProjectsByPriorityList = projectService.countProjectsByPriority(principal.getName());
            return new ResponseEntity<>(countProjectsByPriorityList, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
