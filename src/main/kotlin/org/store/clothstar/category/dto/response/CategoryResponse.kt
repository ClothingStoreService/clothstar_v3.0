package org.store.clothstar.category.dto.response

import org.store.clothstar.category.domain.Category

data class CategoryResponse(
    val categoryId: Long?,
    val categoryType: String?
) {
    companion object {
        fun from(category: Category): CategoryResponse {
            return CategoryResponse(
                categoryId = category.categoryId,
                categoryType = category.categoryType
            )
        }
    }
}