package com.example.inventory.controllers;

import com.example.inventory.entities.InventoryItemData;
import com.example.inventory.services.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping(path = "/inventory/{id}")
    public ResponseEntity<InventoryItemData> getInventoryItemDataById(@PathVariable String id) {
        log.info("Request to get item with id: {}", id);
        Optional<InventoryItemData> itemOptional = inventoryService.getById(id);
        if (itemOptional.isPresent()) {
            log.info("Request success to get item with id: {}!", id);
            return new ResponseEntity<>(itemOptional.get(), HttpStatus.OK);
        }
        log.info("Request to get item with id: {} found nothing!", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
