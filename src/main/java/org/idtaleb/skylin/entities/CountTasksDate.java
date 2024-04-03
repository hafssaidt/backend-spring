package org.idtaleb.skylin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CountTasksDate {
    private LocalDate date;
    private long countTasks;
}
