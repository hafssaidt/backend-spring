package org.idtaleb.skylin.services;


import org.idtaleb.skylin.entities.*;

import java.util.List;

public interface TaskService {
    List<TaskApp> getIndependentTasks(String userId, boolean completed);

    List<TaskApp> getTasksInProject(String projectId, boolean completed);

    TaskApp createIndependentTask(String userId, TaskApp task);

    TaskApp createTaskInKanbanItem(String userId, String kanbanItemId, TaskApp task);

    TaskApp createTaskInProject(String userId, String projectId, TaskApp task);

    TaskApp getTask(String taskId);

    TaskApp updateTask(String taskId, TaskApp task);

    TaskApp updateTaskName(String taskId, String name);

    TaskUpdateManager updateIndependentTaskStatus(String userId, String taskId, Boolean completed);

    TaskUpdateManager updateTaskStatusInProject(String projectId, String taskId, Boolean completed);

    List<TaskApp> updateIndependentTaskOrder(String userId, String taskId, int newOrder);

    List<TaskApp> updateTaskOrderInKanbanItem(String taskId, int newOrder);

    List<TaskApp> updateTaskOrderInProject(String projectId, String taskId, int newOrder);

    List<TaskApp> modifyTasksOrders(List<TaskApp> tasks, TaskApp task, int newOrder);

    KanbanUpdateManager moveTaskToAnotherKanbanItem(String newKanbanItemId, String taskId, int newOrder);

    List<TaskApp> modifyTasksOrdersInNewKanbanItem(List<TaskApp> tasks, int newOrder);

    List<TaskApp> deleteIndependentTask(String userId, String taskId);

    List<TaskApp> deleteTaskInKanbanItem(String taskId);

    List<TaskApp> deleteTaskInProject(String projectId, String taskId);

    List<TaskApp> modifyTasksOrdersAfterDeletionOrMove(List<TaskApp> tasks, int taskOrder);

    void deleteIndependentCompletedTasks(String userId);

    void deleteCompletedTasksInProject(String projectId);

    void deleteAllIndependentTasks(String userId);

    void deleteAllTasksInProject(String projectId);

    CountTask countTasks(String userId);

    List<ProgressProject> countProgressTasksProject(String userId);

    List<CountTasksInKanbanItem> countTasksInKanbanItems(String userId);

    List<CountTasksDate> countTasksByDate(String userId);
}
