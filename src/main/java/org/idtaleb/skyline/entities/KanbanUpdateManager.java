package org.idtaleb.skyline.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class KanbanUpdateManager {
    private List<TaskApp> tasksUpdatedInOldKanbanItem;
    private List<TaskApp> tasksUpdatedInNewKanbanItem;
}
