package org.store.clothstar.order.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.vo.*
import java.time.LocalDate

@Schema(description = "주문 조회용 Response")
class OrderResponse(
    @Schema(description = "주문 id", example = "1")
    val orderId: String,

    @Schema(description = "주문자 이름", example = "수빈")
    val ordererName: String,

    @Schema(description = "주문 생성 날짜", example = "2024-05-15")
    val createdAt: LocalDate,

    @Schema(description = "주문 상태", example = "WAITING")
    val status: Status,

    @Schema(description = "주소 정보")
    val address: AddressDTO,

    @Schema(description = "결제 수단", example = "CARD")
    val paymentMethod: PaymentMethod,

    val totalPrice: TotalPrice,

    var orderDetailList: List<OrderDetailDTO> = ArrayList<OrderDetailDTO>(),
) {

    fun updateOrderDetailList(orderDetailDTOList: List<OrderDetailDTO>) {
        this.orderDetailList = orderDetailDTOList
    }

    companion object {
        fun from(order: Order, member: Member, address: Address): OrderResponse {
            val totalPrice = TotalPrice(
                shipping = order.totalPrice.shipping,
                products = order.totalPrice.products,
                payment = order.totalPrice.payment,
            )

            return OrderResponse(
                orderId = order.orderId,
                ordererName = member.name,
                createdAt = order.createdAt.toLocalDate(),
                status = order.status,
                paymentMethod = order.paymentMethod,
                totalPrice = totalPrice,
                address = AddressDTO(
                    receiverName = address.receiverName,
                    addressBasic = address.addressInfo.addressBasic,
                    addressDetail = address.addressInfo.addressDetail,
                    telNo = address.telNo,
                    deliveryRequest = address.deliveryRequest,
                )
            )
        }
    }
}