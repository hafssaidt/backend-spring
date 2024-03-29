package org.idtaleb.skyline.repositories;

import org.idtaleb.skyline.entities.KanbanItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanbanItemRepository extends CrudRepository<KanbanItem, String> {
    List<KanbanItem> findByUserId(String userId);
}
