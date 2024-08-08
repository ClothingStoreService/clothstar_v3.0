package org.store.clothstar.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.order.domain.Order

interface OrderRepository : JpaRepository<Order, Long> {
    @Query("SELECT o FROM orders o WHERE o.status = 'WAITING' AND o.deletedAt = null")
    fun findWaitingOrders(): List<Order?>

    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status = 'APPROVE' WHERE o.orderId = :orderId")
    fun approveOrder(@Param("orderId") orderId: Long)

    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status = 'CANCEL' WHERE o.orderId = :orderId")
    fun cancelOrder(@Param("orderId") orderId: Long)
}