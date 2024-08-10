package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.NotBlank

class MemberLoginRequest(
    @field: NotBlank(message = "이메일은 필수 입력값 입니다.")
    val email: String,
    @field: NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    val password: String,
) {
    constructor() : this("", "") {
        
    }
}