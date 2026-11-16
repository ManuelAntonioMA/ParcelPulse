package com.parcelpulse.controller

import com.parcelpulse.dto.OrderRequest
import com.parcelpulse.dto.OrderResponse
import com.parcelpulse.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@Valid @RequestBody request: OrderRequest): OrderResponse {
        return orderService.createOrder(request)
    }
}