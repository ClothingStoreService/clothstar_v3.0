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
import org.store.clothstar.order.dto.response.OrderResponse
import org.store.clothstar.order.dto.response.SaveOrderResponse
import org.store.clothstar.order.service.OrderUserService

@Tag(name = "Order", description = "주문(Order) 정보 관리에 대한 API 입니다.")
@RestController
@RequestMapping("/v1/orders")
class OrderUserController(
    private val orderService: OrderUserService
) {
//    @Operation(summary = "단일 주문 조회", description = "단일 주문의 정보를 조회한다.")
//    @GetMapping("/{orderId}")
//    fun getOrder(@PathVariable orderId: String): ResponseEntity<OrderResponse>  {
//        val orderResponse: OrderResponse = orderService.getOrder(orderId)
//        return ResponseEntity.ok(orderResponse)
//    }

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
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문이 정상적으로 구매 확정 되었습니다.",
                content = [Content(schema = Schema(implementation = MessageDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "존재하지 않는 주문번호입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "주문이 '배송완료' 상태가 아니므로 요청을 처리할 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @PatchMapping("{orderId}/complete")
    fun confirmOrder(@PathVariable orderId: String): ResponseEntity<MessageDTO>  {
        orderService.completeOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 구매 확정 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }

    @Operation(summary = "구매자 주문 취소", description = "구매자가 주문 취소 시, 주문상태가 '주문취소'로 변경된다(단, 주문상태가 '승인대기' 또는 '주문승인'일 때만 가능).")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문이 정상적으로 취소되었습니다.",
                content = [Content(schema = Schema(implementation = MessageDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "존재하지 않는 주문번호입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "주문이 '입금확인' 상태가 아니므로 요청을 처리할 수 없습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @PatchMapping("{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: String): ResponseEntity<MessageDTO> {
        orderService.cancelOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문이 정상적으로 삭제되었습니다.",
                content = [Content(schema = Schema(implementation = MessageDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "존재하지 않는 주문번호입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @Operation(summary = "주문 삭제", description = "주문 삭제시간을 현재시간으로 업데이트 한다.")
    @DeleteMapping("{orderId}")
    fun deleteOrder(@PathVariable orderId: String): ResponseEntity<MessageDTO> {
        orderService.updateDeleteAt(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 삭제되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }
}