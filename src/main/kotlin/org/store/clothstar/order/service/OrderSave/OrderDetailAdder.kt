package org.store.clothstar.order.service.OrderSave

import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail

interface OrderDetailAdder {
    fun addOrderDetail(order: Order, orderDetail: OrderDetail)
}