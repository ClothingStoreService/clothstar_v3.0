package org.store.clothstar.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.order.domain.Order

interface OrderUserRepository : JpaRepository<Order, Long> {
    fun findByOrderId(orderId: Long): Order?
}