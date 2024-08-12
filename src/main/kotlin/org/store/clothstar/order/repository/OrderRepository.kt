package org.store.clothstar.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.store.clothstar.order.domain.Order
import java.util.*

interface OrderRepository : JpaRepository<Order, String> {
    fun findByOrderIdAndDeletedAtIsNull(orderId: String): Order?

    @Query("SELECT o FROM orders o WHERE o.status = 'CONFIRMED' AND o.deletedAt is null")
    fun findConfirmedAndNotDeletedOrders(): List<Order>
}