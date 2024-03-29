package org.idtaleb.skyline.services;

import org.idtaleb.skyline.entities.KanbanItem;

import java.util.List;

public interface KanbanItemService {
    List<KanbanItem> getAllKanbanItems(String userId);

    KanbanItem createKanbanItem(String userId, KanbanItem kanbanItem);

    KanbanItem getKanbanItem(String kanbanItemId);

    KanbanItem updateKanbanItemName(String kanbanItemId, String name);

    List<KanbanItem> updateKanbanItemOrder(String userId, String kanbanItemId, int newOrder);

    List<KanbanItem> modifyKanbanItemsOrders(List<KanbanItem> kanbanItems, KanbanItem kanbanItem, int newOrder);

    List<KanbanItem> deleteKanbanItem(String userId, String kanbanItemId);

    List<KanbanItem> modifyKanbanItemsOrdersAfterDeletion(List<KanbanItem> kanbanItems, int kanbanItemOrder);
}
