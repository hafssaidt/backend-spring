package org.idtaleb.skyline.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskUpdateManager {
    private List<TaskApp> tasksUpdatedAfterTaskMove;
    private TaskApp movedTaskUpdated;
}