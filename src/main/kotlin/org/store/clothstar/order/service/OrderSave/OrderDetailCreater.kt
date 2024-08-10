package org.store.clothstar.order.service.OrderSave

import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product

interface OrderDetailCreater {
    fun createOrderDetail(createOrderDetailRequest: CreateOrderDetailRequest, order: Order, product: Product, item: Item): OrderDetail
}