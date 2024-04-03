package org.idtaleb.skylin.services;

import org.idtaleb.skylin.entities.CountProjectsByPriority;
import org.idtaleb.skylin.entities.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects(String userId);

    Project createProject(String userId, Project project);

    Project getProject(String projectId);

    Project updateProject(String projectId, Project project);

    Project updateProjectName(String projectId, String name);

    List<Project> updateProjectOrder(String userÌd, String projectId, int newOrder);

    List<Project> modifyProjectsOrders(List<Project> projects, Project project, int newOrder);

    List<Project> deleteProject(String userId, String projectId);

    List<Project> modifyProjectsOrdersAfterDeletion(List<Project> projects, int projectOrder);

    long countProjects(String userId);

    List<CountProjectsByPriority> countProjectsByPriority(String userId);

}
