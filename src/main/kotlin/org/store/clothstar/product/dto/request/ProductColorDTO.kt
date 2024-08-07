package org.store.clothstar.product.dto.request

import jakarta.validation.constraints.NotBlank
import org.store.clothstar.product.domain.type.ProductColor

class ProductColorDTO(
    @NotBlank(message = "색상을 선택해주세요")
    val color: ProductColor
) {
    fun toProductColor(): ProductColor {
        return color
    }
}