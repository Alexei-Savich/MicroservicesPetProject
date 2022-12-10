package com.example.inventory.repositories;

import com.example.inventory.entities.InventoryItemData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItemData, String> {
}
