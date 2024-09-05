package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class CreateKakaoMemberRequest(
    @field: Email(message = "유효하지 않은 이메일 형식입니다.")
    val email: String,

    @field: NotBlank(message = "이름은 비어 있을 수 없습니다.")
    val name: String,

    @field: Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    val telNo: String,
)