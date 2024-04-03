package org.idtaleb.skylin.controllers;

import org.idtaleb.skylin.entities.KanbanItem;
import org.idtaleb.skylin.services.KanbanItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/kanbanItems")
public class KanbanItemController {

    @Autowired
    KanbanItemService kanbanItemService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllKanbanItemsByUserId(Principal principal) {
        try {
            List<KanbanItem> kanbanItems = kanbanItemService.getAllKanbanItems(principal.getName());
            return new ResponseEntity<>(kanbanItems, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createKanbanItem(Principal principal, @RequestBody KanbanItem kanbanItem) {
        try {
            KanbanItem kanbanItemCreated = kanbanItemService.createKanbanItem(principal.getName(), kanbanItem);
            return new ResponseEntity<>(kanbanItemCreated, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{kanbanItemId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKanbanItemName(@PathVariable String kanbanItemId, @RequestParam String name) {
        try {
            KanbanItem kanbanItemUpdated = kanbanItemService.updateKanbanItemName(kanbanItemId, name);
            return new ResponseEntity<>(kanbanItemUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{kanbanItemId}/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateKanbanItemOrder(Principal principal, @PathVariable String kanbanItemId, @RequestParam int newOrder) {
        try {
            List<KanbanItem> kanbanItemUpdated = kanbanItemService.updateKanbanItemOrder(principal.getName(), kanbanItemId, newOrder);
            return new ResponseEntity<>(kanbanItemUpdated, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{kanbanItemId}")
    public ResponseEntity<?> deleteKanbanItem(Principal principal, @PathVariable String kanbanItemId) {
        try {
            List<KanbanItem> kanbanItems = kanbanItemService.deleteKanbanItem(principal.getName(), kanbanItemId);
            return new ResponseEntity<>(kanbanItems, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
