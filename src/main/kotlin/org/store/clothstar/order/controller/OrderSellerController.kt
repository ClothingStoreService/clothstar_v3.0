package org.store.clothstar.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.store.clothstar.common.dto.ErrorResponseDTO
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.order.dto.response.OrderResponse
import org.store.clothstar.order.service.OrderSellerService

@Tag(name = "OrderSeller", description = "판매자(OrderSeller)의 주문 정보 관리에 대한 API 입니다.")
@RestController
@RequestMapping("/v1/orders/seller")
class OrderSellerController(
    private val orderSellerService: OrderSellerService
) {

    @Operation(summary = "(판매자) WAITING 주문 리스트 조회", description = "(판매자) 주문상태가 '승인대기'인 주문 리스트를 조회한다.")
    @GetMapping
    fun getWaitingOrder(): ResponseEntity<List<OrderResponse>> {
        val orderResponseList: List<OrderResponse> = orderSellerService.getWaitingOrder()
        return ResponseEntity.ok(orderResponseList)
    }

    @Operation(summary = "판매자 주문 승인", description = "판매자가 주문을 출고처리한다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문이 정상적으로 출고처리 되었습니다.",
                content = [Content(schema = Schema(implementation = MessageDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "존재하지 않는 주문번호입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "주문이 입금확인 상태가 아니므로 요청을 처리할 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @PatchMapping("/{orderId}/process")
    fun approveOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
        orderSellerService.approveOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 출고처리 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }

    @Operation(summary = "판매자 주문 취소", description = "판매자가 주문을 승인한다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문이 정상적으로 승인 되었습니다.",
                content = [Content(schema = Schema(implementation = MessageDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "존재하지 않는 주문번호입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "주문이 승인대기 상태가 아니므로 요청을 처리할 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @PatchMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
        orderSellerService.cancelOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }
}
