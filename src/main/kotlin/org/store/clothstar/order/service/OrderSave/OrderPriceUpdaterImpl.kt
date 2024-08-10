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

        System.out.print("!!!!!!!!!!!!!!!!!!!!!! 업데이트 상품 가격=" + updatedTotalProductsPrice)
        System.out.print("!!!!!!!!!!!!!!!!!!!!!! 업데이트 상품 가격=" + updatedTotalPaymentPrice)

        order.totalPrice.updatePrices(updatedTotalProductsPrice, updatedTotalPaymentPrice)

        System.out.print("!!!!!!!!!!!!!!!!!!!!!! 업데이트 상품 가격=" + order.totalPrice.products)
    }
}