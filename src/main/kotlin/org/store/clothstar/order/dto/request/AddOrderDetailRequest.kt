package org.store.clothstar.order.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Schema(description = "주문 상세 추가용 Request")
class AddOrderDetailRequest(
    @Schema(description = "주문 번호")
    @NotNull(message = "주문 번호는 비어있을 수 없습니다.")
    val orderId: Long,

    @Schema(description = "상품 번호")
    @NotNull(message = "상품 번호는 비어있을 수 없습니다.")
    val productLineId: Long,

    @Schema(description = "상품 옵션 번호")
    @NotNull(message = "상품 옵션 번호는 비어있을 수 없습니다.")
    val productId: Long,

    @Schema(description = "상품 수량")
    @NotNull(message = "상품 수량은 비어있을 수 없습니다.")
    @Positive(message = "상품 수량은 0보다 커야 합니다.")
    val quantity: Int = 0,
) {
//    fun toOrderDetail(order: Order, product: Product, item: Item): OrderDetail {
//        val price: Price = Price.builder()
//            .fixedPrice(productLineEntity.getPrice())
//            .oneKindTotalPrice(quantity * productLineEntity.getPrice())
//            .build()
//
//        return OrderDetail.builder()
//            .order(order)
//            .productLineId(productLineEntity.getProductLineId())
//            .productId(productEntity.getProductId())
//            .quantity(quantity)
//            .price(price)
//            .build()
//    }
}
