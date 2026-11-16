package com.parcelpulse.service

import com.parcelpulse.domain.OrderStatus
import com.parcelpulse.domain.Shipment
import com.parcelpulse.domain.ShipmentStatus
import com.parcelpulse.repository.OrderRepository
import com.parcelpulse.repository.ShipmentRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class FulfillmentService(
    private val orderRepository: OrderRepository,
    private val shipmentRepository: ShipmentRepository
) {

    // Runs automatically every 10,000 milliseconds (10 seconds)
    @Scheduled(fixedRate = 10000)
    @Transactional
    fun processPendingOrders() {
        val pendingOrders = orderRepository.findByStatus(OrderStatus.PLACED)
        
        if (pendingOrders.isEmpty()) return
        
        println("📦 Warehouse worker woke up and found ${pendingOrders.size} pending orders. Packing boxes...")

        for (order in pendingOrders) {
            // 1. Mark the order as shipped
            order.status = OrderStatus.SHIPPED
            
            // 2. Generate a tracking number
            val trackingStr = "TRK-${UUID.randomUUID().toString().substring(0, 8).uppercase()}"
            
            // 3. Create the Shipment record
            val shipment = Shipment(
                orderId = order.id,
                status = ShipmentStatus.IN_TRANSIT,
                trackingNumber = trackingStr
            )
            
            shipmentRepository.save(shipment)
            orderRepository.save(order) // Saves the new SHIPPED status
            
            println("✅ Order ${order.id} shipped! Tracking Number: $trackingStr")
        }
    }
}