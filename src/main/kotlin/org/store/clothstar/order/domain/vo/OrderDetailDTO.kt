package org.store.clothstar.order.domain.vo

import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product

class OrderDetailDTO(
    val orderDetailId: Long,
    val productName: String, // 상품명
    val optionName: String,
    val brandName: String,
    val productPrice: Int = 0, // 고정된 상품 가격 ( 주문 당시 가격 )
    val extraCharge: Int = 0,
    val quantity: Int = 0,
    val totalPrice: Int = 0, // 상품 종류 하나당 총 가격
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

