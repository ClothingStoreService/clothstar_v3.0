package org.store.clothstar.order.service.OrderSave

import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.product.domain.Item

interface OrderDetailValidater {
    fun validateOrder(request: CreateOrderDetailRequest, item: Item)
}