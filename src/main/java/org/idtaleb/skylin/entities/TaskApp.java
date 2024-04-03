package org.idtaleb.skylin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "tasks")
public class TaskApp {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "boolean default false")
    private boolean completed;
    @Column(nullable = false)
    private int taskOrder;
    @Column(nullable = false)
    private LocalDate creationDate;
    @Column(nullable = true)
    private LocalDate startDate;
    @Column(nullable = true)
    private LocalDate endDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private org.idtaleb.skylin.entities.UserApp user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "kanban_item_id")
    private org.idtaleb.skylin.entities.KanbanItem kanbanItem;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    private org.idtaleb.skylin.entities.Project project;
    @OneToMany(mappedBy = "task", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<org.idtaleb.skylin.entities.SubTask> subTasks;

    public TaskApp(String name, LocalDate endDate) {
        this.name = name;
        this.endDate = endDate;
    }
}
