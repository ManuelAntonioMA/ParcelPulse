package com.parcelpulse.service

import com.parcelpulse.domain.Order
import com.parcelpulse.domain.OrderItem
import com.parcelpulse.dto.OrderRequest
import com.parcelpulse.dto.OrderResponse
import com.parcelpulse.repository.OrderRepository
import com.parcelpulse.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val inventoryService: InventoryService // Inject the new service
) {
    @Transactional
    fun createOrder(request: OrderRequest): OrderResponse {
        val order = Order(customerId = request.customerId)
        var total = BigDecimal.ZERO

        request.items.forEach { itemReq ->
            val product = productRepository.findById(itemReq.productId)
                .orElseThrow { IllegalArgumentException("Product ${itemReq.productId} not found") }
            
            // RESERVE THE INVENTORY FIRST!
            inventoryService.reserveInventory(product.id, itemReq.quantity)

            val orderItem = OrderItem(
                productId = product.id,
                quantity = itemReq.quantity,
                priceAtPurchase = product.price
            )
            order.addItem(orderItem)
            total += product.price.multiply(BigDecimal(itemReq.quantity))
        }

        val savedOrder = orderRepository.save(order)
        return OrderResponse(
            id = savedOrder.id,
            customerId = savedOrder.customerId,
            status = savedOrder.status.name,
            totalAmount = total.toString()
        )
    }
}