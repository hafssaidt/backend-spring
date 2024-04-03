package org.idtaleb.skylin.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@ToString
@Entity(name = "users")
public class UserApp {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    @Column(nullable = false, length = 30)
    private String firstName;
    @Column(nullable = false, length = 30)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(columnDefinition = "boolean default false")
    private boolean emailVerification;
    @Column(nullable = false)
    private LocalDate creationDate;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<org.idtaleb.skylin.entities.KanbanItem> kanbanItems;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<org.idtaleb.skylin.entities.TaskApp> tasks;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<org.idtaleb.skylin.entities.Project> projects;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<org.idtaleb.skylin.entities.SubTask> subTasks;

}
