package com.parcelpulse.service

import com.parcelpulse.domain.Order
import com.parcelpulse.domain.OrderItem
import com.parcelpulse.dto.OrderRequest
import com.parcelpulse.dto.OrderResponse
import com.parcelpulse.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productService: ProductService, // Swapped!
    private val inventoryService: InventoryService
) {
    @Transactional
    fun createOrder(request: OrderRequest): OrderResponse {
        val order = Order(customerId = request.customerId)
        var total = BigDecimal.ZERO

        request.items.forEach { itemReq ->
            // Fetches from Redis automatically if available!
            val product = productService.getProduct(itemReq.productId)
            
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