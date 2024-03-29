package org.idtaleb.skyline.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountTasksInKanbanItem {
    private String kanbanItemName;
    private long countTasks;
}
