package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class CreateAddressRequest(
    @NotBlank(message = "받는 사람 이름은 비어 있을 수 없습니다.")
    private val receiverName: String,

    @NotBlank(message = "우편번호는 비어 있을 수 없습니다.")
    private val zipNo: String,

    @NotBlank(message = "기본 주소는 비어 있을 수 없습니다.")
    private val addressBasic: String,

    @NotBlank(message = "상세 주소를 입력해 주세요.")
    private val addressDetail: String,

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    private val telNo: String,
    private val deliveryRequest: String,
    private val defaultAddress: Boolean,
)