package org.store.clothstar.order.domain.vo

import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product

class OrderDetailDTO(
    private val orderDetailId: Long,
    private val productName: String, // 상품명
    private val optionName: String,
    private val brandName: String,
    private val productPrice: Int = 0, // 고정된 상품 가격 ( 주문 당시 가격 )
    private val extraCharge: Int = 0,
    private val quantity: Int = 0,
    private val totalPrice: Int = 0, // 상품 종류 하나당 총 가격
) {
    companion object {
        fun from(
            orderDetail: OrderDetail,
            item: Item,
            product: Product,
            brandName: String,
        ): OrderDetailDTO {
            return OrderDetailDTO(
                orderDetailId = orderDetail.orderDetailId!!,
                productName = product.name,
                optionName = item.name,
                brandName = brandName,
                productPrice = product.price,
                quantity = orderDetail.quantity,
                totalPrice = orderDetail.price.oneKindTotalPrice,
                extraCharge = item.finalPrice
            )
        }
    }
}

