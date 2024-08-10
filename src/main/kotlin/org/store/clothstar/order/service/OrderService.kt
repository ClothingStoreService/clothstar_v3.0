package org.store.clothstar.order.service

import org.springframework.stereotype.Service
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade

@Service
class OrderService(
    private val orderSaveFacade: OrderSaveFacade
) {
    fun saveOrder(orderRequestWrapper: OrderRequestWrapper): String {
        return orderSaveFacade.saveOrder(orderRequestWrapper)
    }
}