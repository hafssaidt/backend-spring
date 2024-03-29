package org.idtaleb.skyline.servicesImpl;

import org.idtaleb.skyline.entities.SubTask;
import org.idtaleb.skyline.entities.TaskApp;
import org.idtaleb.skyline.entities.UserApp;
import org.idtaleb.skyline.repositories.SubTaskRepository;
import org.idtaleb.skyline.services.SubTaskService;
import org.idtaleb.skyline.services.TaskService;
import org.idtaleb.skyline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubTaskServiceImpl implements SubTaskService {
    @Autowired
    SubTaskRepository subTaskRepository;
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;

    @Override
    public List<SubTask> getAllSubTasks(String taskId) {
        List<SubTask> subTasks = subTaskRepository.findByTaskId(taskId);
        return subTasks;
    }

    @Override
    public SubTask createSubTask(String userId, String taskId, SubTask subTask) {
        TaskApp task = taskService.getTask(taskId);
        UserApp user = userService.getUserById(userId);
        int size = getAllSubTasks(taskId).size();
        subTask.setTask(task);
        subTask.setUser(user);
        subTask.setSubTaskOrder(size);
        subTask.setCreationDate(LocalDate.now());
        SubTask subTaskSaved = subTaskRepository.save(subTask);
        return subTaskSaved;
    }

    @Override
    public SubTask getSubTask(String subTaskId) {
        Optional<SubTask> subTask = subTaskRepository.findById(subTaskId);
        if (subTask.isEmpty())
            throw new RuntimeException("subTask not found!");
        return subTask.get();
    }

    @Override
    public SubTask updateSubTaskName(String subTaskId, String name) {
        SubTask subTaskChecked = getSubTask(subTaskId);
        subTaskChecked.setName(name);
        SubTask subTaskUpdated = subTaskRepository.save(subTaskChecked);
        return subTaskUpdated;
    }

    @Override
    public SubTask updateSubTaskStatus(String subTaskId, boolean completed) {
        SubTask subTaskChecked = getSubTask(subTaskId);
       /* int size = getAllSubTasks(taskId).size();
        subTaskChecked.setSubTaskOrder(size);*/
        subTaskChecked.setCompleted(completed);
        SubTask subTaskUpdated = subTaskRepository.save(subTaskChecked);
        return subTaskUpdated;
    }

    @Override
    public List<SubTask> updateSubTaskOrder(String taskId, String subTaskId, int newOrder) {
        SubTask subTaskChecked = getSubTask(subTaskId);
        List<SubTask> subTasks = getAllSubTasks(taskId);
        List<SubTask> subTasksOrdersUpdated = modifySubTasksOrders(subTasks, subTaskChecked, newOrder);
        List<SubTask> subTasksUpdated = (List<SubTask>) subTaskRepository.saveAll(subTasksOrdersUpdated);
        return subTasksUpdated;
    }

    @Override
    public List<SubTask> modifySubTasksOrders(List<SubTask> subTasks, SubTask subTask, int newOrder) {
        int oldOrder = subTask.getSubTaskOrder();
        if (newOrder < oldOrder) {
            subTasks.forEach(currentTask -> {
                if (currentTask.getSubTaskOrder() >= newOrder && currentTask.getSubTaskOrder() < oldOrder)
                    currentTask.setSubTaskOrder(currentTask.getSubTaskOrder() + 1);
            });
        } else if (newOrder > oldOrder) {
            subTasks.forEach(currentTask -> {
                if (currentTask.getSubTaskOrder() > oldOrder && currentTask.getSubTaskOrder() <= newOrder)
                    currentTask.setSubTaskOrder(currentTask.getSubTaskOrder() - 1);
            });
        }
        int i = subTasks.indexOf(subTask);
        subTasks.get(i).setSubTaskOrder(newOrder);
        return subTasks;
    }

    @Override
    public List<SubTask> deleteSubTask(String taskId, String subTaskId) {
        SubTask subTask = getSubTask(subTaskId);
        subTaskRepository.delete(subTask);
        List<SubTask> subTasks = getAllSubTasks(taskId);
        List<SubTask> subTasksOrdersUpdated = modifySubTasksOrdersAfterDeletion(subTasks, subTask.getSubTaskOrder());
        List<SubTask> subTasksUpdated = (List<SubTask>) subTaskRepository.saveAll(subTasksOrdersUpdated);
        return subTasksUpdated;
    }

    @Override
    public List<SubTask> modifySubTasksOrdersAfterDeletion(List<SubTask> subTasks, int subTaskOrder) {
        subTasks.forEach(currentSubTask -> {
            if (currentSubTask.getSubTaskOrder() > subTaskOrder)
                currentSubTask.setSubTaskOrder(currentSubTask.getSubTaskOrder() - 1);
        });
        return subTasks;
    }

    @Override
    public long countSubTasks(String userId) {
        return subTaskRepository.findByUserId(userId).size();
    }
}