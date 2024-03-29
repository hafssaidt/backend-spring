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
@Entity(name = "projects")
public class Project {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.LOW;
    @Column(nullable = false)
    private int projectOrder;
    @Column(nullable = false)
    private LocalDate creationDate;
    @Column(nullable = true)
    private LocalDate startDate;
    @Column(nullable = true)
    private LocalDate endDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserApp user;
    @OneToMany(mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<TaskApp> tasks;

    public Project(String name,Priority priority,LocalDate endDate) {
        this.name = name;
        this.priority = priority;
        this.endDate = endDate;
    }
}

