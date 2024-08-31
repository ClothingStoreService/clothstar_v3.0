package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class KakaoMemberRequest (
    @field: NotBlank(message = "이름은 비어 있을 수 없습니다.")
    val name: String,

    @field: Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효하지 않은 전화번호 형식입니다.")
    val telNo: String,

    val email: String?,
) {
    fun addEmail(email: String): KakaoMemberRequest {
        return KakaoMemberRequest(name, telNo, email)
    }
}