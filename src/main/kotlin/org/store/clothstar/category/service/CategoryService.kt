package org.store.clothstar.category.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.category.dto.request.CreateCategoryRequest
import org.store.clothstar.category.dto.request.UpdateCategoryRequest
import org.store.clothstar.category.dto.response.CategoryResponse
import org.store.clothstar.category.repository.CategoryJpaRepository

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
        val category = createCategoryRequest.toCategoryEntity()
        val savedCategory = categoryRepository.save(category)
        return savedCategory.categoryId
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