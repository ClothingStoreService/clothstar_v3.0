package org.store.clothstar.order.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class TotalPrice(
    @Column(name = "total_shipping_price")
    var shipping: Int = 3000,

    @Column(name = "total_products_price")
    var products: Int = 0,

    @Column(name = "total_payment_price")
    var payment: Int = 0
) {
    fun updatePrices(totalProductsPrice: Int, totalPaymentPrice: Int) {
        this.products = totalProductsPrice
        this.payment = totalPaymentPrice
    }
}
