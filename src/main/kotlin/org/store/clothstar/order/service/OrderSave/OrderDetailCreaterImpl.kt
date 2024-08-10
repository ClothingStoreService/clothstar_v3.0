package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product

@Service
class OrderDetailCreaterImpl(): OrderDetailCreater {
    override fun createOrderDetail(createDetailOrderRequest: CreateOrderDetailRequest, order: Order, product: Product, item: Item): OrderDetail {
        return createDetailOrderRequest.toOrderDetail(order, product, item)
    }
}