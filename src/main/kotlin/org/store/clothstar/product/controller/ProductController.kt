package org.store.clothstar.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.store.clothstar.product.dto.response.ProductListResponse
import org.store.clothstar.product.dto.response.ProductResponse
import org.store.clothstar.product.service.ProductApplicationService

@Tag(name = "Products", description = "Products(상품 옵션) 관련 API 입니다.")
@RequestMapping("/v3/products")
@RestController
private class ProductController(
    private val productApplicationService: ProductApplicationService,
) {
    @PostMapping
    @Operation(
        summary = "상품 등록",
        description = "카테고리 아이디, 상품 이름, 내용, 가격, 상태를 입력하여 상품을 신규 등록한다."
    )
    fun createProduct(
        @RequestPart(value = "mainImage", required = false) mainImage: MultipartFile,
        @RequestPart(value = "subImages", required = false) subImages: List<MultipartFile>?,
        @RequestPart(value = "dto") @Validated productCreateRequest: ProductCreateRequest
    ): ResponseEntity<MessageDTO> {
        // 상품 등록
        productApplicationService.createProduct(mainImage, subImages, productCreateRequest);

        val messageDTO = MessageDTO(
            HttpStatus.CREATED.value(),
            "상품 생성이 정상적으로 처리됐습니다."
        )

        return ResponseEntity(messageDTO, HttpStatus.CREATED)
    }
    
     */

    // 상품 전체 Offset 페이징 조회
    @GetMapping("/offset")
    @Operation(summary = "상품 전체 Offset 페이징 조회", description = "상품 전체 리스트를 Offset 페이징 형식으로 조회한다.")
    fun getAllProductsOffsetPaging(
        @PageableDefault(size = 18) pageable: Pageable,
        @RequestParam(required = false) keyword: String?
    ): ResponseEntity<Page<ProductListResponse>> {
        val productPages = productApplicationService.getAllProductsOffsetPaging(pageable, keyword)
        return ResponseEntity.ok().body(productPages)
    }

    // 상품 전체 Slice 페이징 조회
    @GetMapping("/slice")
    @Operation(summary = "상품 전체 Slice 페이징 조회", description = "상품 전체 리스트를 Slice 페이징 형식으로 조회한다.")
    fun getAllProductsSlicePaging(
        @PageableDefault(size = 18) pageable: Pageable,
        @RequestParam(required = false) keyword: String?
    ): ResponseEntity<Slice<ProductListResponse>> {
        val productPages = productApplicationService.getAllProductsSlicePaging(pageable, keyword)
        return ResponseEntity.ok().body(productPages)
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID를 사용하여 특정 상품의 상세 정보를 조회한다.")
    fun getProductDetails(@PathVariable productId: Long): ResponseEntity<ProductResponse> {
        val productResponse = productApplicationService.getProductDetails(productId)
        return ResponseEntity(productResponse, HttpStatus.OK)
    }
}