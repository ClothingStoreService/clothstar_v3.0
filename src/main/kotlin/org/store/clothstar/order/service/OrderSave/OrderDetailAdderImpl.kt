package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail

@Service
class OrderDetailAdderImpl() : OrderDetailAdder {
    override fun addOrderDetail(order: Order, orderDetail: OrderDetail) {
        return order.addOrderDetail(orderDetail)
    }
}