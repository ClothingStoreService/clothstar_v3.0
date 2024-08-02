package org.store.clothstar.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.order.service.OrderSellerService

@Tag(name = "OrderSeller", description = "판매자(OrderSeller)의 주문 정보 관리에 대한 API 입니다.")
@RestController
@RequestMapping("/v1/orders/seller")
class OrderSellerController(
    private val orderSellerService: OrderSellerService
) {

    @Operation(summary = "(판매자) 주문 승인", description = "(판매자) 주문을 승인한다.")
    @PatchMapping("/{orderId}/approve")
    fun approveOrder(@PathVariable orderId: Long): ResponseEntity<MessageDTO> {
        val messageDTO = orderSellerService.approveOrder(orderId)
        return ResponseEntity.ok(messageDTO)
    }
}
