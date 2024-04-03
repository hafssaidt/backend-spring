package org.idtaleb.skylin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity(name = "sub_tasks")
public class SubTask {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean completed;
    @Column(nullable = false)
    private int subTaskOrder;
    @Column(nullable = false)
    private LocalDate creationDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_id")
    private org.idtaleb.skylin.entities.TaskApp task;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private org.idtaleb.skylin.entities.UserApp user;

    public SubTask(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }
}
