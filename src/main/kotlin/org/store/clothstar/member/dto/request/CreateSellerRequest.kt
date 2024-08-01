package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class CreateSellerRequest(
    @NotBlank(message = "브랜드 이름은 필수 입력값 입니다.")
    private val brandName: String,

    @Pattern(regexp = "([0-9]{3})-?([0-9]{2})-?([0-9]{5})", message = "유효하지 않은 사업자 번호 형식입니다.")
    private val bizNo: String,
)