package org.store.clothstar.category.domain

import jakarta.persistence.*
import org.store.clothstar.category.dto.request.UpdateCategoryRequest
import org.store.clothstar.common.domain.BaseTimeEntity

@Entity
data class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long? = null,

    @Column(nullable = false, unique = true)
    var categoryType: String

) : BaseTimeEntity() {

    fun updateCategory(updateCategoryRequest: UpdateCategoryRequest) {
        this.categoryType = updateCategoryRequest.categoryType
    }
}