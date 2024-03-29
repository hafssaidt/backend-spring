package org.idtaleb.skyline.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "kanban_items")
public class KanbanItem {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(nullable = false)
    private int KanbanItemOrder;
    @Column(nullable = false)
    private LocalDate creationDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserApp user;
    @OneToMany(mappedBy = "kanbanItem", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<TaskApp> tasks;

    public KanbanItem(String name) {
        this.name = name;
    }
}
