package org.store.clothstar.order.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.store.clothstar.order.domain.Order

interface OrderRepository : JpaRepository<Order, String> {
    fun findByOrderIdAndDeletedAtIsNull(orderId: String): Order?

    @Query("SELECT o FROM orders o WHERE o.status = 'CONFIRMED' AND o.deletedAt is null")
    fun findConfirmedAndNotDeletedOrders(): List<Order>

    fun findAllByOrderByOrderIdDesc(pageable: Pageable): Slice<Order>
}