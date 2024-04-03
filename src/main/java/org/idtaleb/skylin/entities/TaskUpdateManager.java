package org.idtaleb.skylin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskUpdateManager {
    private List<org.idtaleb.skylin.entities.TaskApp> tasksUpdatedAfterTaskMove;
    private org.idtaleb.skylin.entities.TaskApp movedTaskUpdated;
}