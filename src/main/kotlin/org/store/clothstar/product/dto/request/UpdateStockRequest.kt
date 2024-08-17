package org.store.clothstar.product.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero

class UpdateStockRequest (

    @field:NotNull(message = "재고 수량은 필수 입력 사항입니다.")
    @field:PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.")
    val stock: Int
)