//package org.store.clothstar.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.service.OrderDetailService

//@Tag(name = "OrderDetail", description = "주문 내 개별 상품에 대한 옵션, 수량 등을 나타내는, 주문상세(OrderDetail) 정보 관리에 대한 API 입니다.")
//@RestController
//class OrderDetailController(
//    private val orderDetailService: OrderDetailService,
//) {
//    @Operation(summary = "주문상세 추가 저장", description = "개별 상품에 대한 주문상세(상품명, 가격, 개수...)를 특정 주문에 추가 저장한다.")
//    @PostMapping
//    fun addOrderDetail(@RequestBody @Validated addOrderDetailRequest: AddOrderDetailRequest): ResponseEntity<String> {
//        val orderDetailId = orderDetailService.addOrderDetail(addOrderDetailRequest)
//
//        return ResponseEntity.ok(
//            SaveResponseDTO(
//                orderDetailId, HttpStatus.OK.value(), "주문상세가 정상적으로 생성되었습니다."
//            )
//        )
//    }
//}