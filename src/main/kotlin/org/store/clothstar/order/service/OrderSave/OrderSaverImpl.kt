package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.repository.OrderRepository

@Service
class OrderSaverImpl(
    private val orderRepository: OrderRepository
): OrderSaver {
    override fun saveOrder(order: Order) {
        orderRepository.save(order)
    }
}