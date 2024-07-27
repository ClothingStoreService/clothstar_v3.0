package org.store.clothstar.category.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.store.clothstar.category.domain.Category

data class CreateCategoryRequest(
    @Schema(description = "카테고리 타입(이름)", nullable = false)
    @field:NotBlank(message = "카테고리 타입을 입력해주세요.")
    val categoryType: String?
) {
    fun toCategoryEntity(): Category {
        return Category(
            categoryType = categoryType ?: ""
        )
    }
}