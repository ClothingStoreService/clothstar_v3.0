package org.store.clothstar.order.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Price(
    @Column(name = "fixed_price")
    val fixedPrice: Int, // 고정된 상품 가격 (주문 당시 가격)

    @Column(name = "onekind_total_price")
    val oneKindTotalPrice: Int // 상품 종류 하나당 총 가격
)
