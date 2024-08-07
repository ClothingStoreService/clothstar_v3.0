package org.store.clothstar.order.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "주문 상세 조회용 Response")
class OrderDetailResponse(
    @Schema(description = "주문 상세 번호", example = "1")
    private val orderDetailId: Long,

    @Schema(description = "주문 번호", example = "1")
    private val orderId: Long,

    @Schema(description = "상품 번호", example = "1")
    private val productLineId: Long,

    @Schema(description = "상품 옵션 번호", example = "1")
    private val productId: Long,

    @Schema(description = "상품 수량", example = "2")
    private val quantity: Int = 0,

    @Schema(description = "고정 가격", example = "15000")
    private val fixedPrice: Int = 0,

    @Schema(description = "상품 종류 하나당 총 가격", example = "30000")
    private val oneKindTotalPrice: Int = 0,

    @Schema(description = "상품 이름", example = "나이키 반팔티")
    private val name: String,

    @Schema(description = "옵션 상품 재고", example = "30")
    private val stock: Long,

    @Schema(description = "옵션 이름", example = "검정")
    private val optionName: String,

    @Schema(description = "옵션 추가 비용", example = "0")
    private val extraCharge: Int,

    @Schema(description = "브랜드 이름", example = "나이키")
    private val brandName: String,
)