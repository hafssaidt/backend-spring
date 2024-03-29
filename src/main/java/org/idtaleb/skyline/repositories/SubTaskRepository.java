package org.idtaleb.skyline.repositories;

import org.idtaleb.skyline.entities.SubTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskRepository extends CrudRepository<SubTask, String> {
    List<SubTask> findByTaskId(String taskId);

    List<SubTask> findByUserId(String userId);

}
