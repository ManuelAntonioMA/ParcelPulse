package com.parcelpulse.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class OrderRequest(
    @field:NotEmpty(message = "Customer ID is required")
    val customerId: String,
    
    @field:NotEmpty(message = "Order must contain at least one item")
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val productId: Long,
    
    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int
)

data class OrderResponse(
    val id: Long,
    val customerId: String,
    val status: String,
    val totalAmount: String
)