package org.idtaleb.skylin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class KanbanUpdateManager {
    private List<org.idtaleb.skylin.entities.TaskApp> tasksUpdatedInOldKanbanItem;
    private List<org.idtaleb.skylin.entities.TaskApp> tasksUpdatedInNewKanbanItem;
}
