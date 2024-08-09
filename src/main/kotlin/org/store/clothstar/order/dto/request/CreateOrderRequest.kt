package org.store.clothstar.order.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.store.clothstar.order.domain.vo.PaymentMethod

@Schema(description = "주문 저장용 Request")
class CreateOrderRequest(
    @Schema(description = "결제 수단")
    private val paymentMethod: @NotNull(message = "결제 수단은 비어있을 수 없습니다.") PaymentMethod,

    @Schema(description = "회원 번호")
    @NotNull(message = "회원 번호는 비어있을 수 없습니다.")
    private val memberId: Long,

    @Schema(description = "배송지 번호")
    @NotNull(message = "배송지 번호는 비어있을 수 없습니다.")
    private val addressId: Long,
) {


//    fun toOrder(member: Member, address: Address): Order {
//        val totalPrice: TotalPrice = TotalPrice.builder()
//            .shipping(3000)
//            .products(0)
//            .payment(0)
//            .build()
//
//        return Order.builder()
//            .orderId(GenerateOrderId.generateOrderId())
//            .memberId(member.memberId)
//            .addressId(address.addressId)
//            .status(Status.WAITING)
//            .paymentMethod(paymentMethod)
//            .totalPrice(totalPrice)
//            .build()
//    }
}