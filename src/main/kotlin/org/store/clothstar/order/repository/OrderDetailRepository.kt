package org.store.clothstar.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.store.clothstar.order.domain.OrderDetail

interface OrderDetailRepository : JpaRepository<OrderDetail, Long> {
    @Query("SELECT od FROM order_detail od WHERE od.order.orderId = :orderId")
    fun findOrderDetailListByOrderId(orderId: String): List<OrderDetail>
}