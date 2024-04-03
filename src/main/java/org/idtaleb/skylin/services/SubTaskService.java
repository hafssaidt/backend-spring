package org.idtaleb.skylin.services;

import org.idtaleb.skylin.entities.SubTask;

import java.util.List;

public interface SubTaskService {
    List<SubTask> getAllSubTasks(String taskId);

    SubTask createSubTask(String userId, String taskId, SubTask subTask);

    SubTask getSubTask(String subTaskId);

    SubTask updateSubTaskName(String subTaskId, String name);

    SubTask updateSubTaskStatus(String subTaskId, boolean completed);

    List<SubTask> updateSubTaskOrder(String taskId, String subTaskId, int newOrder);

    List<SubTask> modifySubTasksOrders(List<SubTask> subTasks, SubTask subTask, int newOrder);

    List<SubTask> deleteSubTask(String taskId, String subTaskId);

    List<SubTask> modifySubTasksOrdersAfterDeletion(List<SubTask> subTasks, int subTaskOrder);

    long countSubTasks(String userId);

}
