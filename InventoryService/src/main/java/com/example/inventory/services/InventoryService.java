package com.example.inventory.services;

import com.example.inventory.entities.InventoryItemData;
import com.example.inventory.repositories.InventoryRepository;
import com.google.common.base.Stopwatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Optional<InventoryItemData> getById(String id) {
//         waiting 5 seconds to test fallback behavior
//         Stopwatch watch = Stopwatch.createStarted();
//         while (watch.elapsed(SECONDS) < 5) {
//         }
        return inventoryRepository.findById(id);
    }

}
