package org.store.clothstar.order.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.Price
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product

@Schema(description = "주문 상세 저장용 Request")
class CreateOrderDetailRequest(
    @Schema(description = "상품 번호")
    @field: NotNull(message = "상품 번호는 비어있을 수 없습니다.")
    val productId: Long,

    @Schema(description = "상품 옵션 번호")
    @field: NotNull(message = "상품 옵션 번호는 비어있을 수 없습니다.")
    val itemId: Long,

    @Schema(description = "상품 수량")
    @field: NotNull(message = "상품 수량은 비어있을 수 없습니다.")
    @field: Positive(message = "상품 수량은 0보다 커야 합니다.")
    val quantity: Int,
) {
    fun toOrderDetail(order: Order, product: Product, item: Item): OrderDetail {
        val price = Price(
            fixedPrice = product.price,
            oneKindTotalPrice = quantity * product.price + quantity * item.finalPrice,
        )

        return OrderDetail(
            order = order,
            productId = productId,
            itemId = itemId,
            quantity = quantity,
            price = price,
        )
    }
}
