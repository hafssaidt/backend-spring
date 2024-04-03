package org.idtaleb.skylin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgressProject {
    private String projectName;
    private long countCompletedTasks;
    private long countIncompleteTasks;
}
