package com.parcelpulse.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime

enum class OrderStatus { PLACED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, FAILED }
enum class ShipmentStatus { PENDING, IN_TRANSIT, DELIVERED }

@Entity
@Table(name = "products")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true, nullable = false)
    val sku: String,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false)
    val price: BigDecimal
)

@Entity
@Table(name = "inventory")
class Inventory(
    @Id
    @Column(name = "product_id")
    val productId: Long,

    @Column(name = "available_quantity", nullable = false)
    var availableQuantity: Int,

    @Version
    @Column(nullable = false)
    var version: Long = 0
)

@Entity
@Table(name = "orders")
class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "customer_id", nullable = false)
    val customerId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.PLACED,

    @Column(name = "idempotency_key", unique = true)
    val idempotencyKey: String? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OrderItem> = mutableListOf(),

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime? = null
) {
    fun addItem(item: OrderItem) {
        items.add(item)
        item.order = this
    }
}

@Entity
@Table(name = "order_items")
class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order? = null,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(nullable = false)
    val quantity: Int,

    @Column(name = "price_at_purchase", nullable = false)
    val priceAtPurchase: BigDecimal
)

@Entity
@Table(name = "shipments")
class Shipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "order_id", nullable = false, unique = true)
    val orderId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ShipmentStatus = ShipmentStatus.PENDING,

    @Column(name = "tracking_number")
    var trackingNumber: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime? = null
)