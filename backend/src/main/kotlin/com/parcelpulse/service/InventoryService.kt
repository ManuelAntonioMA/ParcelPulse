package com.parcelpulse.service

import com.parcelpulse.repository.InventoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryService(private val inventoryRepository: InventoryRepository) {

    @Transactional
    fun reserveInventory(productId: Long, quantity: Int) {
        // 1. Fetch the current inventory
        val inventory = inventoryRepository.findById(productId)
            .orElseThrow { IllegalArgumentException("Inventory record not found for product $productId") }

        // 2. Check if we have enough stock
        if (inventory.availableQuantity < quantity) {
            throw IllegalArgumentException("Not enough stock for product $productId. Requested: $quantity, Available: ${inventory.availableQuantity}")
        }

        // 3. Deduct the stock
        inventory.availableQuantity -= quantity
        
        // 4. Save it back to the database. 
        // Because of the @Version annotation, Hibernate will check if the version number 
        // changed between Step 1 and Step 4. If it did, it throws an OptimisticLockException!
        inventoryRepository.save(inventory)
    }
}