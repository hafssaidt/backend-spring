package org.idtaleb.skylin.servicesImpl;

import org.idtaleb.skylin.entities.*;
import org.idtaleb.skylin.repositories.TaskRepository;
import org.idtaleb.skylin.services.KanbanItemService;
import org.idtaleb.skylin.services.ProjectService;
import org.idtaleb.skylin.services.TaskService;
import org.idtaleb.skylin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserService userService;
    @Autowired
    KanbanItemService kanbanItemService;
    @Autowired
    ProjectService projectService;

    @Override
    public List<TaskApp> getIndependentTasks(String userId, boolean completed) {
        List<TaskApp> tasks = taskRepository.findByUserIdAndKanbanItemIdIsNullAndProjectIdIsNullAndCompleted(userId, completed);
        return tasks;
    }

    @Override
    public List<TaskApp> getTasksInProject(String projectId, boolean completed) {
        List<TaskApp> tasks = taskRepository.findByProjectIdAndCompleted(projectId, completed);
        return tasks;
    }

    @Override
    public TaskApp createIndependentTask(String userId, TaskApp task) {
        UserApp user = userService.getUserById(userId);
        int size = getIndependentTasks(userId, false).size();
        task.setUser(user);
        task.setTaskOrder(size);
        task.setCreationDate(LocalDate.now());
        TaskApp taskSaved = taskRepository.save(task);
        return taskSaved;
    }

    @Override
    public TaskApp createTaskInKanbanItem(String userId, String kanbanItemId, TaskApp task) {
        UserApp user = userService.getUserById(userId);
        KanbanItem kanbanItem = kanbanItemService.getKanbanItem(kanbanItemId);
        List<TaskApp> tasks = kanbanItem.getTasks();
        int size;
        if (tasks == null) {
            size = 0;
        } else size = tasks.size();
        task.setUser(user);
        task.setKanbanItem(kanbanItem);
        task.setTaskOrder(size);
        task.setCreationDate(LocalDate.now());
        TaskApp taskSaved = taskRepository.save(task);
        return taskSaved;
    }

    @Override
    public TaskApp createTaskInProject(String userId, String projectId, TaskApp task) {
        UserApp user = userService.getUserById(userId);
        Project project = projectService.getProject(projectId);
        int size = project.getTasks().size();
        task.setUser(user);
        task.setProject(project);
        task.setTaskOrder(size);
        task.setCreationDate(LocalDate.now());
        TaskApp taskSaved = taskRepository.save(task);
        return taskSaved;
    }

    @Override
    public TaskApp getTask(String taskId) {
        Optional<TaskApp> taskChecked = taskRepository.findById(taskId);
        if (taskChecked.isEmpty())
            throw new RuntimeException("task not found!");
        return taskChecked.get();
    }

    @Override
    public TaskApp updateTask(String taskId, TaskApp task) {
        TaskApp taskChecked = getTask(taskId);
        taskChecked.setName(task.getName());
        taskChecked.setDescription(task.getDescription());
        taskChecked.setStartDate(task.getStartDate());
        taskChecked.setEndDate(task.getEndDate());
        TaskApp taskUpdated = taskRepository.save(taskChecked);
        return taskUpdated;
    }

    @Override
    public TaskApp updateTaskName(String taskId, String name) {
        TaskApp taskChecked = getTask(taskId);
        taskChecked.setName(name);
        TaskApp taskUpdated = taskRepository.save(taskChecked);
        return taskUpdated;
    }

    @Override
    public TaskUpdateManager updateIndependentTaskStatus(String userId, String taskId, Boolean completed) {
        TaskApp taskChecked = getTask(taskId);
        int oldTaskOrder = taskChecked.getTaskOrder();
        boolean oldTaskStatus = taskChecked.isCompleted();

        //set new updates
        int size = getIndependentTasks(userId, completed).size();
        taskChecked.setCompleted(completed);
        taskChecked.setTaskOrder(size);
        TaskApp movedTaskUpdated = taskRepository.save(taskChecked);

        List<TaskApp> tasksWithOldStatus = getIndependentTasks(userId, oldTaskStatus);
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrdersAfterDeletionOrMove(tasksWithOldStatus, oldTaskOrder);

        List<TaskApp> tasksUpdatedAfterTaskMove = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return new TaskUpdateManager(tasksUpdatedAfterTaskMove, movedTaskUpdated);
    }

    @Override
    public TaskUpdateManager updateTaskStatusInProject(String projectId, String taskId, Boolean completed) {
        TaskApp taskChecked = getTask(taskId);
        int oldTaskOrder = taskChecked.getTaskOrder();
        boolean oldTaskStatus = taskChecked.isCompleted();

        //set new updates
        int size = getTasksInProject(projectId, completed).size();
        taskChecked.setCompleted(completed);
        taskChecked.setTaskOrder(size);
        TaskApp movedTaskUpdated = taskRepository.save(taskChecked);

        List<TaskApp> tasksWithOldStatus = getTasksInProject(projectId, oldTaskStatus);
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrdersAfterDeletionOrMove(tasksWithOldStatus, oldTaskOrder);

        List<TaskApp> tasksUpdatedAfterTaskMove = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return new TaskUpdateManager(tasksUpdatedAfterTaskMove, movedTaskUpdated);
    }

    @Override
    public List<TaskApp> updateIndependentTaskOrder(String userId, String taskId, int newOrder) {
        TaskApp taskChecked = getTask(taskId);
        List<TaskApp> tasks = getIndependentTasks(userId, taskChecked.isCompleted());
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrders(tasks, taskChecked, newOrder);
        List<TaskApp> tasksUpdated = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return tasksUpdated;
    }

    @Override
    public List<TaskApp> updateTaskOrderInKanbanItem(String taskId, int newOrder) {
        TaskApp taskChecked = getTask(taskId);
        List<TaskApp> tasks = taskChecked.getKanbanItem().getTasks();
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrders(tasks, taskChecked, newOrder);
        List<TaskApp> tasksUpdated = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return tasksUpdated;
    }

    @Override
    public List<TaskApp> updateTaskOrderInProject(String projectId, String taskId, int newOrder) {
        TaskApp taskChecked = getTask(taskId);
        List<TaskApp> tasks = getTasksInProject(projectId, taskChecked.isCompleted());
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrders(tasks, taskChecked, newOrder);
        List<TaskApp> tasksUpdated = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return tasksUpdated;
    }

    @Override
    public List<TaskApp> modifyTasksOrders(List<TaskApp> tasks, TaskApp task, int newOrder) {
        int oldOrder = task.getTaskOrder();
        if (newOrder < oldOrder) {
            tasks.forEach(currentTask -> {
                if (currentTask.getTaskOrder() >= newOrder && currentTask.getTaskOrder() < oldOrder)
                    currentTask.setTaskOrder(currentTask.getTaskOrder() + 1);
            });
        } else if (newOrder > oldOrder) {
            tasks.forEach(currentTask -> {
                if (currentTask.getTaskOrder() > oldOrder && currentTask.getTaskOrder() <= newOrder)
                    currentTask.setTaskOrder(currentTask.getTaskOrder() - 1);
            });
        }
        int i = tasks.indexOf(task);
        tasks.get(i).setTaskOrder(newOrder);
        return tasks;
    }

    @Override
    public KanbanUpdateManager moveTaskToAnotherKanbanItem(String newKanbanItemId, String taskId, int newOrder) {
        KanbanItem newKanbanItem = kanbanItemService.getKanbanItem(newKanbanItemId);

        TaskApp taskChecked = getTask(taskId);
        List<TaskApp> tasksInOldKanban = taskChecked.getKanbanItem().getTasks();
        List<TaskApp> tasksOrdersUpdatedInOldKanbanItem = modifyTasksOrdersAfterDeletionOrMove(tasksInOldKanban, taskChecked.getTaskOrder());
        tasksOrdersUpdatedInOldKanbanItem.remove(taskChecked);
        List<TaskApp> tasksUpdatedInOldKanbanItem = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdatedInOldKanbanItem);

        List<TaskApp> tasksOrdersUpdatedInNewKanbanItem = modifyTasksOrdersInNewKanbanItem(newKanbanItem.getTasks(), newOrder);
        taskChecked.setKanbanItem(newKanbanItem);
        taskChecked.setTaskOrder(newOrder);
        tasksOrdersUpdatedInNewKanbanItem.add(taskChecked);
        List<TaskApp> tasksUpdatedInNewKanbanItem = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdatedInNewKanbanItem);

        KanbanUpdateManager kanbanUpdateManager = new KanbanUpdateManager(tasksUpdatedInOldKanbanItem, tasksUpdatedInNewKanbanItem);
        return kanbanUpdateManager;
    }

    @Override
    public List<TaskApp> modifyTasksOrdersInNewKanbanItem(List<TaskApp> tasks, int newOrder) {
        tasks.forEach(currentTask -> {
            if (currentTask.getTaskOrder() >= newOrder)
                currentTask.setTaskOrder(currentTask.getTaskOrder() + 1);
        });
        return tasks;
    }

    @Override
    public List<TaskApp> deleteIndependentTask(String userId, String taskId) {
        TaskApp taskChecked = getTask(taskId);
        taskRepository.delete(taskChecked);
        List<TaskApp> tasks = getIndependentTasks(userId, taskChecked.isCompleted());
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrdersAfterDeletionOrMove(tasks, taskChecked.getTaskOrder());
        List<TaskApp> tasksUpdated = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return tasksUpdated;
    }

    @Override
    public List<TaskApp> deleteTaskInKanbanItem(String taskId) {
        TaskApp taskChecked = getTask(taskId);
        taskRepository.delete(taskChecked);
        List<TaskApp> tasks = taskChecked.getKanbanItem().getTasks();
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrdersAfterDeletionOrMove(tasks, taskChecked.getTaskOrder());
        List<TaskApp> tasksUpdated = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return tasksUpdated;
    }

    @Override
    public List<TaskApp> deleteTaskInProject(String projectId, String taskId) {
        TaskApp taskChecked = getTask(taskId);
        taskRepository.delete(taskChecked);
        List<TaskApp> tasks = getTasksInProject(projectId, taskChecked.isCompleted());
        List<TaskApp> tasksOrdersUpdated = modifyTasksOrdersAfterDeletionOrMove(tasks, taskChecked.getTaskOrder());
        List<TaskApp> tasksUpdated = (List<TaskApp>) taskRepository.saveAll(tasksOrdersUpdated);
        return tasksUpdated;
    }

    @Override
    public List<TaskApp> modifyTasksOrdersAfterDeletionOrMove(List<TaskApp> tasks, int taskOrder) {
        tasks.forEach(currentTask -> {
            if (currentTask.getTaskOrder() > taskOrder)
                currentTask.setTaskOrder(currentTask.getTaskOrder() - 1);
        });
        return tasks;
    }

    @Override
    public void deleteIndependentCompletedTasks(String userId) {
        taskRepository.deleteByUserIdAndKanbanItemIdIsNullAndProjectIdIsNullAndCompletedIsTrue(userId);
    }

    @Override
    public void deleteCompletedTasksInProject(String projectId) {
        taskRepository.deleteByProjectIdAndCompletedIsTrue(projectId);
    }

    @Override
    public void deleteAllIndependentTasks(String userId) {
        taskRepository.deleteByUserIdAndKanbanItemIdIsNullAndProjectIdIsNull(userId);
    }

    @Override
    public void deleteAllTasksInProject(String projectId) {
        taskRepository.deleteByProjectId(projectId);
    }

    @Override
    public CountTask countTasks(String userId) {
        CountTask countTask = new CountTask();
        countTask.setCountIncomplete(taskRepository.findByUserIdAndCompleted(userId, false).size());
        countTask.setCountCompleted(taskRepository.findByUserIdAndCompleted(userId, true).size());
        return countTask;
    }

    @Override
    public List<ProgressProject> countProgressTasksProject(String userId) {
        return taskRepository.countProgressTasksProject(userId);
    }

    @Override
    public List<CountTasksInKanbanItem> countTasksInKanbanItems(String userId) {
        return taskRepository.countTasksInKanbanItems(userId);
    }

    @Override
    public List<CountTasksDate> countTasksByDate(String userId) {
        return taskRepository.countTasksByDate(userId);
    }

}