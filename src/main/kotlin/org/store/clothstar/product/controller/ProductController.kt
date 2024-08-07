package org.store.clothstar.product.controller

import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.category.service.CategoryService
import org.store.clothstar.product.service.ProductService
import java.net.URI

@Tag(name = "Products", description = "Products(상품 옵션) 관련 API 입니다.")
@RequestMapping("/v3/products")
@RestController
private class ProductController (
    private val productApplicationService: ProductApplicationService,
    private val productService: ProductService,
    private val categoryService: CategoryService,
) {
    @PostMapping
    @Operation(summary = "상품 등록",
        description = "카테고리 아이디, 상품 이름, 내용, 가격, 상태를 입력하여 상품을 신규 등록한다.")
    fun createProduct(
        @Validated @RequestBody productCreateRequest: ProductCreateRequest
    ) : ResponseEntity<MessageDTO> {
        // 상품 등록

        // 최종 product 생성
        productApplicationService.createProduct(productCreateRequest);

    }
}