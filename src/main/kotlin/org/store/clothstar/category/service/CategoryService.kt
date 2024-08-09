package org.store.clothstar.category.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.category.domain.Category
import org.store.clothstar.category.dto.request.CreateCategoryRequest
import org.store.clothstar.category.dto.request.UpdateCategoryRequest
import org.store.clothstar.category.dto.response.CategoryResponse
import org.store.clothstar.category.repository.CategoryJpaRepository
import org.store.clothstar.common.util.Logger.Companion.log

@Service
class CategoryService(
    private val categoryRepository: CategoryJpaRepository
) {
    @Transactional(readOnly = true)
    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().map { CategoryResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getCategory(categoryId: Long): CategoryResponse {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리를 찾을 수 없습니다.") }
        return CategoryResponse.from(category)
    }

    @Transactional
    fun createCategory(createCategoryRequest: CreateCategoryRequest): Long? {
        val category = Category(categoryType = createCategoryRequest.categoryType) // dto의 메서드로 entity로 변환 -> ?? 코틀린에서는
        val savedCategory = categoryRepository.save(category)
        return savedCategory.categoryId
            .also {
                log.info { "Category created, id : ${category.categoryId}" }
            }
    }

    @Transactional
    fun updateCategory(categoryId: Long, updateCategoryRequest: UpdateCategoryRequest) {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리를 찾을 수 없습니다.") }
        category.updateCategory(updateCategoryRequest)
    }

    @Transactional
    fun deleteCategory(categoryId: Long) {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리를 찾을 수 없습니다.") }
        categoryRepository.delete(category)
    }
}