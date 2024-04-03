package org.idtaleb.skylin.repositories;

import org.idtaleb.skylin.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<TaskApp, String> {
    List<TaskApp> findByUserIdAndKanbanItemIdIsNullAndProjectIdIsNullAndCompleted(String userId, boolean completed);

    List<TaskApp> findByProjectIdAndCompleted(String projectId, boolean completed);

    List<TaskApp> findByKanbanItem(KanbanItem kanbanItem);

    @Transactional
    void deleteByUserIdAndKanbanItemIdIsNullAndProjectIdIsNullAndCompletedIsTrue(String userId);

    @Transactional
    void deleteByUserIdAndKanbanItemIdIsNullAndProjectIdIsNull(String userId);

    @Transactional
    void deleteByProjectIdAndCompletedIsTrue(String projectId);

    @Transactional
    void deleteByProjectId(String projectId);

    List<TaskApp> findByUserIdAndCompleted(String userId, boolean completed);

    @Query("SELECT NEW org.idtaleb.skylin.entities.ProgressProject(p.name, " +
            "COUNT(CASE WHEN t.completed = true THEN 1 END), " +
            "COUNT(CASE WHEN t.completed = false THEN 1 END)) " +
            "FROM projects p, tasks t " +
            "WHERE t.user.id = :userId AND t.project.id = p.id " +
            "GROUP BY p.id, p.name")
    List<ProgressProject> countProgressTasksProject(@Param("userId") String userId);

    @Query("SELECT NEW org.idtaleb.skylin.entities.CountTasksInKanbanItem(k.name, COUNT(t)) FROM tasks t, kanban_items k WHERE t.user.id = :userId AND t.kanbanItem.id = k.id GROUP BY k.id,k.name ORDER BY k.KanbanItemOrder")
    List<CountTasksInKanbanItem> countTasksInKanbanItems(@Param("userId") String userId);

    @Query("SELECT NEW org.idtaleb.skylin.entities.CountTasksDate(t.creationDate, COUNT(t)) FROM tasks t WHERE t.user.id = :userId GROUP BY t.creationDate ORDER BY t.creationDate")
    List<CountTasksDate> countTasksByDate(@Param("userId") String userId);
}
