package com.parcelpulse.controller

import com.parcelpulse.domain.Product
import com.parcelpulse.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Long): Product {
        return productService.getProduct(id)
    }
}