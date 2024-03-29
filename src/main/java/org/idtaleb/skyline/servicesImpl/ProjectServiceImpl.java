package org.idtaleb.skyline.servicesImpl;

import org.idtaleb.skyline.entities.CountProjectsByPriority;
import org.idtaleb.skyline.entities.Project;
import org.idtaleb.skyline.entities.UserApp;
import org.idtaleb.skyline.repositories.ProjectRepository;
import org.idtaleb.skyline.services.ProjectService;
import org.idtaleb.skyline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserService userService;

    @Override
    public List<Project> getAllProjects(String userId) {
        List<Project> projects = projectRepository.findByUserId(userId);
        return projects;
    }

    @Override
    public Project createProject(String userId, Project project) {
        UserApp user = userService.getUserById(userId);
        int size = getAllProjects(userId).size();
        project.setUser(user);
        project.setProjectOrder(size);
        project.setCreationDate(LocalDate.now());
        Project projectSaved = projectRepository.save(project);
        return projectSaved;
    }

    @Override
    public Project getProject(String projectId) {
        Optional<Project> projectChecked = projectRepository.findById(projectId);
        if (projectChecked.isEmpty())
            throw new RuntimeException("project not found!");
        return projectChecked.get();
    }

    @Override
    public Project updateProject(String projectId, Project project) {
        Project projectChecked = getProject(projectId);

        projectChecked.setName(project.getName());
        projectChecked.setDescription(project.getDescription());
        projectChecked.setPriority(project.getPriority());
        projectChecked.setStartDate(project.getStartDate());
        projectChecked.setEndDate(project.getEndDate());
        Project projectUpdated = projectRepository.save(projectChecked);
        return projectUpdated;
    }

    @Override
    public Project updateProjectName(String projectId, String name) {
        Project projectChecked = getProject(projectId);
        projectChecked.setName(name);
        Project projectUpdated = projectRepository.save(projectChecked);
        return projectUpdated;
    }

    @Override
    public List<Project> updateProjectOrder(String userÌd, String projectId, int newOrder) {
        Project projectChecked = getProject(projectId);
        List<Project> projects = getAllProjects(userÌd);
        List<Project> projectsOrdersUpdated = modifyProjectsOrders(projects, projectChecked, newOrder);
        List<Project> projectsUpdated = (List<Project>) projectRepository.saveAll(projectsOrdersUpdated);
        return projectsUpdated;
    }

    @Override
    public List<Project> modifyProjectsOrders(List<Project> projects, Project project, int newOrder) {
        int oldOrder = project.getProjectOrder();
        if (newOrder < oldOrder) {
            projects.forEach(currentProject -> {
                if (currentProject.getProjectOrder() >= newOrder && currentProject.getProjectOrder() < oldOrder)
                    currentProject.setProjectOrder(currentProject.getProjectOrder() + 1);
            });
        } else if (newOrder > oldOrder) {
            projects.forEach(currentProject -> {
                if (currentProject.getProjectOrder() > oldOrder && currentProject.getProjectOrder() <= newOrder)
                    currentProject.setProjectOrder(currentProject.getProjectOrder() - 1);
            });
        }
        int i = projects.indexOf(project);
        projects.get(i).setProjectOrder(newOrder);
        return projects;
    }

    @Override
    public List<Project> deleteProject(String userId, String projectId) {
        Project projectChecked = getProject(projectId);
        projectRepository.delete(projectChecked);
        List<Project> projects = getAllProjects(userId);
        List<Project> projectsOrdersUpdated = modifyProjectsOrdersAfterDeletion(projects, projectChecked.getProjectOrder());
        List<Project> projectsUpdated = (List<Project>) projectRepository.saveAll(projectsOrdersUpdated);
        return projectsUpdated;
    }

    @Override
    public List<Project> modifyProjectsOrdersAfterDeletion(List<Project> projects, int projectOrder) {
        projects.forEach(currentProject -> {
            if (currentProject.getProjectOrder() > projectOrder)
                currentProject.setProjectOrder(currentProject.getProjectOrder() - 1);
        });
        return projects;
    }

    @Override
    public long countProjects(String userId) {
        return projectRepository.findByUserId(userId).size();
    }

    @Override
    public List<CountProjectsByPriority> countProjectsByPriority(String userId) {
        List<CountProjectsByPriority> countProjectsByPriorityList = projectRepository.countProjectsByPriority(userId);
        return countProjectsByPriorityList;
    }
}
