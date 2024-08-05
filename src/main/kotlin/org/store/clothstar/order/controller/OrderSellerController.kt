package org.store.clothstar.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.order.exception.OrderErrorResponseDTO
import org.store.clothstar.order.service.OrderSellerService

@Tag(name = "OrderSeller", description = "판매자(OrderSeller)의 주문 정보 관리에 대한 API 입니다.")
@RestController
@RequestMapping("/v1/orders/seller")
class OrderSellerController(
    private val orderSellerService: OrderSellerService
) {

    @Operation(summary = "판매자 주문 승인", description = "판매자가 주문을 승인한다.")
    @ApiResponses( value = [
        ApiResponse(responseCode = "200", description = "주문이 정상적으로 승인 되었습니다.",
            content = [Content(schema = Schema(implementation = MessageDTO::class))]),
        ApiResponse(responseCode = "400", description = "존재하지 않는 주문번호입니다.",
            content = [Content(schema = Schema(implementation = OrderErrorResponseDTO::class))]),
        ApiResponse(responseCode = "404", description = "주문이 승인대기 상태가 아니므로 요청을 처리할 수 없습니다.",
            content = [Content(schema = Schema(implementation = OrderErrorResponseDTO::class))]),
        ]
    )
    @PatchMapping("/{orderId}/approve")
    fun approveOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
        orderSellerService.approveOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }

    @Operation(summary = "판매자 주문 취소", description = "판매자가 주문을 승인한다.")
    @ApiResponses( value = [
        ApiResponse(responseCode = "200", description = "주문이 정상적으로 승인 되었습니다.",
            content = [Content(schema = Schema(implementation = MessageDTO::class))]),
        ApiResponse(responseCode = "400", description = "존재하지 않는 주문번호입니다.",
            content = [Content(schema = Schema(implementation = OrderErrorResponseDTO::class))]),
        ApiResponse(responseCode = "404", description = "주문이 승인대기 상태가 아니므로 요청을 처리할 수 없습니다.",
            content = [Content(schema = Schema(implementation = OrderErrorResponseDTO::class))]),
        ]
    )
    @PatchMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
        orderSellerService.cancelOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }
}
