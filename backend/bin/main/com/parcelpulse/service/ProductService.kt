package com.parcelpulse.service

import com.parcelpulse.domain.Product
import com.parcelpulse.repository.ProductRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    // The @Cacheable annotation intercepts the request. 
    // If "products::id" is in Redis, it returns it instantly without running this function.
    @Cacheable(value = ["products"], key = "#id")
    fun getProduct(id: Long): Product {
        println("=======> FETCHING PRODUCT $id FROM POSTGRESQL (CACHE MISS) <=======")
        
        return productRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Product $id not found") }
    }
}