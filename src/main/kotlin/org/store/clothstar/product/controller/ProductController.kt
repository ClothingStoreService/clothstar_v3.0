package org.store.clothstar.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.store.clothstar.category.service.CategoryService
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.service.ProductApplicationService
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
        @RequestPart(value = "mainImage", required = false) mainImage: MultipartFile,
        @RequestPart(value = "subImages", required = false) subImages: List<MultipartFile>?,
        @RequestPart(value = "dto") @Validated productCreateRequest: ProductCreateRequest
    ) : ResponseEntity<MessageDTO> {
        // 상품 등록
        productApplicationService.createProduct(mainImage, subImages, productCreateRequest);

        val messageDTO = MessageDTO(
            HttpStatus.CREATED.value(),
            "상품 생성이 정상적으로 처리됐습니다."
        )

        return ResponseEntity(messageDTO, HttpStatus.CREATED)

    }
}