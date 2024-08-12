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
import org.store.clothstar.common.dto.SaveResponseDTO
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.dto.response.OrderResponse
import org.store.clothstar.order.dto.response.SaveOrderResponse
import org.store.clothstar.order.service.OrderUserService

@Tag(name = "Order", description = "주문(Order) 정보 관리에 대한 API 입니다.")
@RestController
@RequestMapping("/v1/orders")
class OrderUserController(
    private val orderUserService: OrderUserService,
) {
    @Operation(summary = "단일 주문 조회", description = "단일 주문의 정보를 조회한다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "",
                content = [Content(schema = Schema(implementation = SaveOrderResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: String):ResponseEntity<OrderResponse>  {
        val orderResponse: OrderResponse = orderUserService.getOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }

//    @Operation(summary = "전체 주문 조회 offset 페이징", description = "전체 주문 리스트를 offset 페이징 형식으로 가져온다.")
//    @GetMapping("/offset")
//    public ResponseEntity<Page<OrderResponse>> getAllOrderOffsetPaging(
//    @PageableDefault(size = 15) Pageable pageable) {
//        Page<OrderResponse> orderPages = orderService.getAllOrderOffsetPaging(pageable);
//        return ResponseEntity.ok(orderPages);
//    }
//
//    @Operation(summary = "전체 주문 조회 slice 페이징", description = "전체 주문 리스트를 slice 페이징 형식으로 가져온다.")
//    @GetMapping("/slice")
//    public ResponseEntity<Slice<OrderResponse>> getAllOrderSlicePaging(
//    @PageableDefault(size = 15) Pageable pageable) {
//        Slice<OrderResponse> orderPages = orderService.getAllOrderSlicePaging(pageable);
//        return ResponseEntity.ok(orderPages);
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
        val orderId: String = orderUserService.saveOrder(orderRequestWrapper)
        val saveOrderResponse = SaveOrderResponse(orderId, HttpStatus.OK.value(), "주문이 정상적으로 생성되었습니다.")
        return ResponseEntity.ok(saveOrderResponse)
    }

    @Operation(summary = "주문상세 추가 저장", description = "개별 상품에 대한 주문상세(상품명, 가격, 개수...)를 특정 주문에 추가 저장한다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "주문상세가 정상적으로 생성되었습니다.",
                content = [Content(schema = Schema(implementation = SaveOrderResponse::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "",
                content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
            ),
        ]
    )
    @PostMapping("/details")
    fun addOrderDetail(@RequestBody @Validated addOrderDetailRequest: AddOrderDetailRequest): ResponseEntity<SaveResponseDTO> {
        val orderDetailId = orderUserService.addOrderDetail(addOrderDetailRequest)
        val saveResponseDTO = SaveResponseDTO(orderDetailId, HttpStatus.OK.value(), "주문상세가 정상적으로 생성되었습니다.")
        return ResponseEntity.ok(saveResponseDTO)
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
    fun confirmOrder(@PathVariable orderId: String): ResponseEntity<MessageDTO> {
        orderUserService.completeOrder(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 구매 확정 되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }

    @Operation(
        summary = "구매자 주문 취소",
        description = "구매자가 주문 취소 시, 주문상태가 '주문취소'로 변경된다(단, 주문상태가 '승인대기' 또는 '주문승인'일 때만 가능)."
    )
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
        orderUserService.cancelOrder(orderId)
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
        orderUserService.updateDeleteAt(orderId)
        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 삭제되었습니다.")
        return ResponseEntity.ok(messageDTO)
    }
}