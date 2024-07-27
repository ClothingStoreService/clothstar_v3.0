package org.store.clothstar.category.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.store.clothstar.category.dto.request.CreateCategoryRequest
import org.store.clothstar.category.dto.request.UpdateCategoryRequest
import org.store.clothstar.category.dto.response.CategoryResponse
import org.store.clothstar.category.service.CategoryService
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.common.util.URIBuilder

@RestController
@RequestMapping("/v1/categories")
class CategoryController(
    private val categoryService: CategoryService,
//    private val productLineService: ProductLineService
) {

    @Operation(summary = "전체 카테고리 조회", description = "모든 카테고리를 조회한다.")
    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryResponse>> {
        val categoryResponses = categoryService.getAllCategories()
        return ResponseEntity.ok(categoryResponses)
    }

    @Operation(summary = "카테고리 상세 조회", description = "id로 카테고리 한개를 상세 조회한다.")
    @GetMapping("/{categoryId}")
    fun getCategory(@PathVariable categoryId: Long): ResponseEntity<CategoryResponse> {
        val categoryDetailResponse = categoryService.getCategory(categoryId)
        return ResponseEntity.ok(categoryDetailResponse)
    }

    @Operation(summary = "카테고리 등록", description = "카테고리 타입(이름)을 입력하여 신규 카테고리를 등록한다.")
    @PostMapping
    fun createCategory(@Validated @RequestBody createCategoryRequest: CreateCategoryRequest): ResponseEntity<Void> {
        val categoryId = categoryService.createCategory(createCategoryRequest)
        val location = URIBuilder.buildURI(categoryId)
        return ResponseEntity.created(location).build()
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 이름을 수정한다.")
    @PutMapping("/{categoryId}")
    fun updateCategories(
        @PathVariable categoryId: Long,
        @Validated @RequestBody updateCategoryRequest: UpdateCategoryRequest
    ): ResponseEntity<MessageDTO> {
        categoryService.updateCategory(categoryId, updateCategoryRequest)
        return ResponseEntity.ok(MessageDTO(HttpStatus.OK.value(), "Category updated successfully"))
    }

    @Operation(summary = "카테고리 삭제", description = "id로 카테고리를 삭제한다.")
    @DeleteMapping("/{categoryId}")
    fun deleteCategory(@PathVariable categoryId: Long): ResponseEntity<MessageDTO> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity.ok(MessageDTO(HttpStatus.OK.value(), "Category deleted successfully"))
    }
}