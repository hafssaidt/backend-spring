package org.idtaleb.skylin.servicesImpl;

import org.idtaleb.skylin.entities.KanbanItem;
import org.idtaleb.skylin.entities.UserApp;
import org.idtaleb.skylin.repositories.KanbanItemRepository;
import org.idtaleb.skylin.services.KanbanItemService;
import org.idtaleb.skylin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class KanbanItemServiceImpl implements KanbanItemService {
    @Autowired
    KanbanItemRepository kanbanItemRepository;
    @Autowired
    UserService userService;

    @Override
    public List<KanbanItem> getAllKanbanItems(String userId) {
        List<KanbanItem> kanbanItems = kanbanItemRepository.findByUserId(userId);
        return kanbanItems;
    }

    @Override
    public KanbanItem createKanbanItem(String userId, KanbanItem kanbanItem) {
        UserApp user = userService.getUserById(userId);
        int size = getAllKanbanItems(userId).size();
        kanbanItem.setUser(user);
        kanbanItem.setKanbanItemOrder(size);
        kanbanItem.setCreationDate(LocalDate.now());
        KanbanItem kanbanItemSaved = kanbanItemRepository.save(kanbanItem);
        return kanbanItemSaved;
    }

    @Override
    public KanbanItem getKanbanItem(String kanbanItemId) {
        Optional<KanbanItem> kanbanItem = kanbanItemRepository.findById(kanbanItemId);
        if (kanbanItem.isEmpty())
            throw new RuntimeException("kanbanItem not found!");
        return kanbanItem.get();
    }

    @Override
    public KanbanItem updateKanbanItemName(String kanbanItemId, String name) {
        KanbanItem kanbanItem = getKanbanItem(kanbanItemId);
        kanbanItem.setName(name);
        KanbanItem kanbanItemUpdated = kanbanItemRepository.save(kanbanItem);
        return kanbanItemUpdated;
    }

    @Override
    public List<KanbanItem> updateKanbanItemOrder(String userId, String kanbanItemId, int newOrder) {
        KanbanItem kanbanItem = getKanbanItem(kanbanItemId);
        List<KanbanItem> kanbanItems = kanbanItemRepository.findByUserId(userId);
        List<KanbanItem> kanbanItemsOrdersUpdated = modifyKanbanItemsOrders(kanbanItems, kanbanItem, newOrder);
        List<KanbanItem> kanbanItemsUpdated = (List<KanbanItem>) kanbanItemRepository.saveAll(kanbanItemsOrdersUpdated);
        return kanbanItemsUpdated;
    }

    @Override
    public List<KanbanItem> modifyKanbanItemsOrders(List<KanbanItem> kanbanItems, KanbanItem kanbanItem, int newOrder) {
        int oldOrder = kanbanItem.getKanbanItemOrder();
        if (newOrder < oldOrder) {
            kanbanItems.forEach(currentTask -> {
                if (currentTask.getKanbanItemOrder() >= newOrder && currentTask.getKanbanItemOrder() < oldOrder)
                    currentTask.setKanbanItemOrder(currentTask.getKanbanItemOrder() + 1);
            });
        } else if (newOrder > oldOrder) {
            kanbanItems.forEach(currentTask -> {
                if (currentTask.getKanbanItemOrder() > oldOrder && currentTask.getKanbanItemOrder() <= newOrder)
                    currentTask.setKanbanItemOrder(currentTask.getKanbanItemOrder() - 1);
            });
        }
        int i = kanbanItems.indexOf(kanbanItem);
        kanbanItems.get(i).setKanbanItemOrder(newOrder);
        return kanbanItems;
    }

    @Override
    public List<KanbanItem> deleteKanbanItem(String userId, String kanbanItemId) {
        KanbanItem kanbanItem = getKanbanItem(kanbanItemId);
        kanbanItemRepository.delete(kanbanItem);
        List<KanbanItem> kanbanItems = kanbanItemRepository.findByUserId(userId);
        List<KanbanItem> kanbanItemsOrdersUpdated = modifyKanbanItemsOrdersAfterDeletion(kanbanItems, kanbanItem.getKanbanItemOrder());
        List<KanbanItem> kanbanItemsUpdated = (List<KanbanItem>) kanbanItemRepository.saveAll(kanbanItemsOrdersUpdated);
        return kanbanItemsUpdated;
    }

    @Override
    public List<KanbanItem> modifyKanbanItemsOrdersAfterDeletion(List<KanbanItem> kanbanItems, int kanbanItemOrder) {
        kanbanItems.forEach(currentKanbanItem -> {
            if (currentKanbanItem.getKanbanItemOrder() > kanbanItemOrder)
                currentKanbanItem.setKanbanItemOrder(currentKanbanItem.getKanbanItemOrder() - 1);
        });
        return kanbanItems;
    }

}
