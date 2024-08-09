package org.store.clothstar.order.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class TotalPrice(
    @Column(name = "total_shipping_price", nullable = false)
    var shipping: Int = 3000, // 기본 배송비는 3000원.

    @Column(name = "total_products_price", nullable = false)
    var products: Int = 0,

    @Column(name = "total_payment_price", nullable = false)
    var payment: Int = 0
) {
    fun updatePrices(totalProductsPrice: Int, totalPaymentPrice: Int) {
        this.products = totalProductsPrice
        this.payment = totalPaymentPrice
    }
}
