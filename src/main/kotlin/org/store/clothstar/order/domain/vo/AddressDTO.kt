package org.store.clothstar.order.domain.vo

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "배송지 정보")
class AddressDTO(
    @Schema(description = "수령인 이름", example = "수빈")
    val receiverName: String,

    @Schema(description = "기본 주소", example = "서울시 강남구")
    val addressBasic: String,

    @Schema(description = "상세 주소", example = "123-456")
    val addressDetail: String,

    @Schema(description = "전화번호", example = "010-1234-5678")
    val telNo: String,

    @Schema(description = "배송 요청 사항", example = "문 앞에 놓아주세요.")
    val deliveryRequest: String,
)
