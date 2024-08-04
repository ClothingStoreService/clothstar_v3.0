package org.store.clothstar.product.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ItemCreateRequest (
    @NotBlank(message = "아이템 이름을 입력해주세요")
    val name: String
)