package org.idtaleb.skyline.repositories;

import org.idtaleb.skyline.entities.CountProjectsByPriority;
import org.idtaleb.skyline.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {
    List<Project> findByUserId(String userId);

    //List<Project> findByUserIdAndPriority(String userId, Priority priority);
    @Query("SELECT NEW org.idtaleb.skyline.entities.CountProjectsByPriority(p.priority, COUNT(p)) FROM projects p WHERE p.user.id = :userId GROUP BY p.priority")
    List<CountProjectsByPriority> countProjectsByPriority(@Param("userId") String userId);

}
