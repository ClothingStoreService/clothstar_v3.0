package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.Size

class ModifyPasswordRequest(
    @field: Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    var password: String,
)