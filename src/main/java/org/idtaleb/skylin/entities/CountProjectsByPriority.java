package org.idtaleb.skylin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountProjectsByPriority {
    private Priority priority;
    private long count;

}
