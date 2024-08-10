package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail

@Service
class OrderPriceUpdaterImpl() : OrderPriceUpdater {
    override fun updateOrderPrice(order: Order, orderDetail: OrderDetail) {
        val updatedTotalProductsPrice: Int = order.totalPrice.products + orderDetail.price.oneKindTotalPrice
        val updatedTotalPaymentPrice: Int =
            order.totalPrice.products + order.totalPrice.shipping + orderDetail.price.oneKindTotalPrice

        order.totalPrice.updatePrices(updatedTotalProductsPrice, updatedTotalPaymentPrice)
    }
}