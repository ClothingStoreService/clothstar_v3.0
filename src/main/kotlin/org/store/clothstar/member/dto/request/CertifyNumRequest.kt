package org.store.clothstar.member.dto.request

import jakarta.validation.constraints.NotNull

class CertifyNumRequest(
    @NotNull(message = "이메일을 입력해 주세요")
    val email: String
)