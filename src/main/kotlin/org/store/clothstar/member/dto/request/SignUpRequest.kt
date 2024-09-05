package org.store.clothstar.member.dto.request

import jakarta.validation.Valid

class SignUpRequest(
    @field: Valid
    val createMemberRequest: CreateMemberRequest?,
    var kakaoMemberRequest: KakaoMemberRequest?
)