package org.store.clothstar.order.service.OrderSave

import org.store.clothstar.order.domain.Order

interface OrderSaver {
    fun saveOrder(order: Order)
}