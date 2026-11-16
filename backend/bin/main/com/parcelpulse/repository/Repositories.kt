package com.parcelpulse.repository

import com.parcelpulse.domain.Inventory
import com.parcelpulse.domain.Order
import com.parcelpulse.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>
interface InventoryRepository : JpaRepository<Inventory, Long>
interface OrderRepository : JpaRepository<Order, Long>