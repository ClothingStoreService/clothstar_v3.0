package org.store.clothstar.category.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.store.clothstar.category.domain.Category
import org.store.clothstar.category.dto.request.CreateCategoryRequest
import org.store.clothstar.category.dto.request.UpdateCategoryRequest
import org.store.clothstar.category.repository.CategoryJpaRepository
import java.util.*
import kotlin.test.Test

class CategoryServiceTest {

    @Mock
    private lateinit var categoryRepository: CategoryJpaRepository

    @InjectMocks
    private lateinit var categoryService: CategoryService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getAllCategories should return list of CategoryResponse`() {
        // Given
        val categories = listOf(
            Category(categoryId = 1, categoryType = "Type1"),
            Category(categoryId = 2, categoryType = "Type2")
        )
        `when`(categoryRepository.findAll()).thenReturn(categories)

        // When
        val result = categoryService.getAllCategories()

        // Then
        assertEquals(2, result.size)
        assertEquals("Type1", result[0].categoryType)
        assertEquals("Type2", result[1].categoryType)
    }

    @Test
    fun `getCategory should return CategoryResponse for valid id`() {
        // Given
        val category = Category(categoryId = 1, categoryType = "Type1")
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.of(category))

        // When
        val result = categoryService.getCategory(1L)

        // Then
        assertEquals("Type1", result.categoryType)
    }

    @Test
    fun `createCategory should save and return new category id`() {
        // Given
        val category = Category(categoryId = null, categoryType = "Type1")
        val savedCategory = Category(categoryId = 1, categoryType = "Type1")
        `when`(categoryRepository.save(category)).thenReturn(savedCategory)

        // When
        val result = categoryService.createCategory(CreateCategoryRequest(categoryType = "Type1"))

        // Then
        assertEquals(1L, result)
        verify(categoryRepository, times(1)).save(any(Category::class.java))
    }

    @Test
    fun `updateCategory should update category`() {
        // Given
        val category = Category(categoryId = 1, categoryType = "Type1")
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.of(category))

        val updateCategoryRequest = UpdateCategoryRequest(categoryType = "UpdatedType")

        // When
        categoryService.updateCategory(1L, updateCategoryRequest)

        // Then
        verify(categoryRepository, times(1)).findById(1L)
        // 추가적인 확인 로직을 필요시 추가
    }

    @Test
    fun `deleteCategory should delete category`() {
        // Given
        val category = Category(categoryId = 1, categoryType = "Type1")
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.of(category))

        // When
        categoryService.deleteCategory(1L)

        // Then
        verify(categoryRepository, times(1)).delete(category)
    }
}