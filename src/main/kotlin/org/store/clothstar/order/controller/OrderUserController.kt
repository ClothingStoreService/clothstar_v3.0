//package org.store.clothstar.order.controller
//
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.tags.Tag
//import jakarta.mail.Message
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.Pageable
//import org.springframework.data.domain.Slice
//import org.springframework.data.web.PageableDefault
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.validation.annotation.Validated
//import org.springframework.web.bind.annotation.*
//import org.store.clothstar.common.dto.MessageDTO
//import org.store.clothstar.common.dto.SaveResponseDTO
//import org.store.clothstar.order.dto.request.OrderRequestWrapper
//import org.store.clothstar.order.dto.response.OrderResponse
//import org.store.clothstar.order.service.OrderService
//
//@Tag(name = "Order", description = "주문(Order) 정보 관리에 대한 API 입니다.")
//@RestController
//@RequestMapping("/v1/orders")
//class OrderUserController(
//    private val orderService: OrderService,
//) {
//
//    @Operation(summary = "단일 주문 조회", description = "단일 주문의 정보를 조회한다.")
//    @GetMapping("/{orderId}")
//    fun getOrder(@PathVariable orderId: Long): ResponseEntity<OrderResponse> {
//        val orderResponse = orderService.getOrder(orderId)
//        return ResponseEntity.ok(orderResponse)
//    }
//
//    @Operation(summary = "전체 주문 조회 offset 페이징", description = "전체 주문 리스트를 offset 페이징 형식으로 가져온다.")
//    @GetMapping("/offset")
//    fun getAllOrderOffsetPaging(@PageableDefault(size = 15) pageable: Pageable): ResponseEntity<Page<OrderResponse>> {
//        val orderPages = orderService.getAllOrderOffsetPaging(pageable)
//        return ResponseEntity.ok(orderPages)
//    }
//
//    @Operation(summary = "전체 주문 조회 slice 페이징", description = "전체 주문 리스트를 slice 페이징 형식으로 가져온다.")
//    @GetMapping("/slice")
//    fun getAllOrderSlicePaging(@PageableDefault(size = 15) pageable: Pageable): ResponseEntity<Slice<OrderResponse>> {
//        val orderPages = orderService.getAllOrderSlicePaging(pageable)
//        return ResponseEntity.ok(orderPages)
//    }
//
//    @Operation(summary = "주문 생성", description = "단일 주문을 생성한다.")
//    @PostMapping
//    fun saveOrder(@RequestBody @Validated orderRequestWrapper: OrderRequestWrapper): ResponseEntity<SaveResponseDTO> {
//        val orderId = orderService.saveOrder(orderRequestWrapper)
//        val saveMessageDTO = SaveResponseDTO(orderId, HttpStatus.OK.value(), "주문이 정상적으로 생성되었습니다.")
//        return ResponseEntity.ok(saveMessageDTO)
//    }
//
//    @Operation(summary = "(구매자)구매 확정", description = "구매자가 구매 확정 시, 주문상태가 '구매확정'으로 변경된다(단, 주문상태가 '배송완료'일 때만 가능).")
//    @PatchMapping("{orderId}/confirm")
//    fun confirmOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
//        orderService.confirmOrder(orderId)
//        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 구매 확정 되었습니다.")
//        return ResponseEntity.ok(messageDTO)
//    }
//
//    @Operation(
//        summary = "(구매자)주문 취소",
//        description = "구매자가 주문 취소 시, 주문상태가 '주문취소'로 변경된다(단, 주문상태가 '승인대기' 또는 '주문승인'일 때만 가능)."
//    )
//    @PatchMapping("{orderId}/cancel")
//    fun cancelOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
//        orderService.cancelOrder(orderId)
//        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소되었습니다.")
//        return ResponseEntity.ok(messageDTO)
//    }
//
//    @Operation(summary = "주문 삭제", description = "주문 삭제시간을 현재시간으로 업데이트 한다.")
//    @DeleteMapping("{orderId}")
//    fun deleteOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
//        orderService.updateDeleteAt(orderId)
//        val messageDTO = MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 삭제되었습니다.")
//        return ResponseEntity.ok(messageDTO)
//    }
//}