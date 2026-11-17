package com.parcelpulse.service

import com.parcelpulse.repository.InventoryRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException

@SpringBootTest
class InventoryConcurrencyTest {

    @Autowired
    lateinit var inventoryRepository: InventoryRepository

    @Test
    fun `concurrent inventory updates should trigger optimistic locking exception`() {
        // 1. ALICE AND BOB BOTH VIEW THE SAME ITEM AT THE EXACT SAME MILLISECOND
        // Both read the database and see version = 0, availableQuantity = 15
        val inventoryAlice = inventoryRepository.findById(3L).get()
        val inventoryBob = inventoryRepository.findById(3L).get()

        // 2. ALICE BUYS 5 HEADPHONES
        inventoryAlice.availableQuantity -= 5
        // Alice saves successfully. The database updates her stock to 10, and increments version to 1.
        inventoryRepository.save(inventoryAlice)

        // 3. BOB TRIES TO BUY 5 HEADPHONES A MILLISECOND LATER
        inventoryBob.availableQuantity -= 5
        
        // Bob's object still thinks the version is 0. 
        // When he tries to save, Hibernate sees the database is actually at version 1.
        // We wrap Bob's save in assertThrows to prove the backend violently rejects it!
        assertThrows(ObjectOptimisticLockingFailureException::class.java) {
            inventoryRepository.save(inventoryBob)
        }
        
        println("✅ SUCCESS: Optimistic Locking prevented Bob from overselling the inventory!")
    }
}