package org.store.clothstar.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.store.clothstar.common.dto.ErrorResponseDTO
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.dto.response.SaveOrderResponse
import org.store.clothstar.order.service.OrderService

@Tag(name = "Order", description = "주문(Order) 정보 관리에 대한 API 입니다.")
@RestController
@RequestMapping("/v1/orders")
class OrderUserController(
    private val orderService: OrderService
) {
    @Operation(summary = "주문 생성", description = "단일 주문을 생성한다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문이 정상적으로 생성되었습니다.",
                content = [Content(schema = Schema(implementation = SaveOrderResponse::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "품절된 상품입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "주문 개수가 상품 재고보다 더 많아 요청을 처리할 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @PostMapping
    fun saveOrder(@RequestBody @Validated orderRequestWrapper: OrderRequestWrapper): ResponseEntity<SaveOrderResponse> {
        val orderId: String = orderService.saveOrder(orderRequestWrapper)
        val saveOrderResponse = SaveOrderResponse(orderId, HttpStatus.OK.value(), "주문이 정상적으로 생성되었습니다.")
        return ResponseEntity.ok(saveOrderResponse)
    }

    @Operation(summary = "구매자 구매 확정", description = "구매자가 주문을 구매확정하면, 주문상태가 '구매확정'으로 변경된다(단, 주문상태가 '배송완료'일 때만 가능).")
    @PatchMapping("{orderId}/complete")
    fun confirmOrder(@PathVariable orderId: String): ResponseEntity<MessageDTO>  {
        orderService.completeOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 구매 확정 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }

    @Operation(summary = "구매자 주문 취소", description = "구매자가 주문 취소 시, 주문상태가 '주문취소'로 변경된다(단, 주문상태가 '승인대기' 또는 '주문승인'일 때만 가능).")
    @PatchMapping("{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: String): ResponseEntity<MessageDTO> {
        orderService.cancelOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }
}