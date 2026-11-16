package com.parcelpulse.repository

import com.parcelpulse.domain.Inventory
import com.parcelpulse.domain.Order
import com.parcelpulse.domain.OrderStatus
import com.parcelpulse.domain.Product
import com.parcelpulse.domain.Shipment
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>
interface InventoryRepository : JpaRepository<Inventory, Long>
interface ShipmentRepository : JpaRepository<Shipment, Long>

interface OrderRepository : JpaRepository<Order, Long> {
    // Spring automatically writes the SQL query just based on this function name!
    fun findByStatus(status: OrderStatus): List<Order>
}