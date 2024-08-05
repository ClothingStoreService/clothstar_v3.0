package org.store.clothstar.product.controller

import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.category.service.CategoryService
import org.store.clothstar.product.service.ProductService

@RestController
class ProductController (
    private val productService: ProductService,
    private val categoryService: CategoryService,
) {
}