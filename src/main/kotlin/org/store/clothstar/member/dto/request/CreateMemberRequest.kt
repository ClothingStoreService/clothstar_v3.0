package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.*

class CreateMemberRequest(
    @field: Email(message = "유효하지 않은 이메일 형식입니다.")
    val email: String,

    @field: Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    val password: String,

    @field: NotBlank(message = "이름은 비어 있을 수 없습니다.")
    val name: String,

    @field: Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    val telNo: String,

    @field: NotNull(message = "인증번호를 입력해 주세요")
    val certifyNum: String,
)