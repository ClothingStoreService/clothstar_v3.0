package org.store.clothstar.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.dto.request.UpdateDisplayStatusRequest
import org.store.clothstar.product.dto.request.UpdateStockRequest
import org.store.clothstar.product.dto.response.ProductResponse
import org.store.clothstar.product.service.ProductApplicationService

@Tag(name = "ProductSellers", description = "ProductSellers(판매자) 관련 API 입니다.")
@RestController
@RequestMapping("/v3/sellers/products")
class ProductSellerController(
    private val productApplicationService: ProductApplicationService
) {
    // 판매자 상품 등록
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

    // 상품 상세 조회
    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID를 사용하여 해당 상품의 상세 정보를 조회합니다.")
    fun getProductDetails(@PathVariable productId: Long): ResponseEntity<ProductResponse> {
        val productResponse = productApplicationService.getProductDetails(productId, true)
        return ResponseEntity.ok(productResponse)
    }

    @PatchMapping("/{productId}/displayStatus")
    @Operation(summary = "상품 진열 상태 변경", description = "상품 ID를 사용하여 해당 상품의 진열 상태를 변경합니다.")
    fun updateProductDisplayStatus(
        @PathVariable productId: Long,
        @RequestBody request: UpdateDisplayStatusRequest
    ): ResponseEntity<MessageDTO> {
        productApplicationService.updateProductDisplayStatus(productId, request.displayStatus)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "상품 진열 상태가 성공적으로 변경되었습니다."
        )
        return ResponseEntity(messageDTO, HttpStatus.OK)
    }

    @PatchMapping("/{productId}/items/{itemId}/displayStatus")
    @Operation(summary = "아이템 진열 상태 변경", description = "상품 ID와 아이템 ID를 사용하여 해당 아이템의 진열 상태를 변경합니다.")
    fun updateItemDisplayStatus(
        @PathVariable productId: Long,
        @PathVariable itemId: Long,
        @RequestBody request: UpdateDisplayStatusRequest
    ): ResponseEntity<MessageDTO> {
        productApplicationService.updateItemDisplayStatus(productId, itemId, request.displayStatus)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "아이템 진열 상태가 성공적으로 변경되었습니다."
        )
        return ResponseEntity(messageDTO, HttpStatus.OK)
    }

    // 상품 재고 수량 변경
    @PatchMapping("/{productId}/items/{itemId}/stock")
    @Operation(summary = "아이템 재고 수량 변경", description = "상품 ID와 아이템 ID를 사용하여 해당 아이템의 재고 수량을 변경합니다.")
    fun updateItemStock(
        @PathVariable productId: Long,
        @PathVariable itemId: Long,
        @RequestBody request: UpdateStockRequest
    ): ResponseEntity<MessageDTO> {
        productApplicationService.updateItemStock(productId, itemId, request.stock)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "아이템 재고 수량이 성공적으로 변경되었습니다."
        )
        return ResponseEntity(messageDTO, HttpStatus.OK)
    }

    /*
    // 상품 기본 정보 수정
    @PutMapping("/{productId}")
    @Operation(summary = "상품 기본 정보 수정", description = "상품 ID를 사용하여 해당 상품의 기본 정보를 수정합니다.")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestPart(value = "mainImage", required = false) mainImage: MultipartFile?,
        @RequestPart(value = "subImages", required = false) subImages: List<MultipartFile>?,
        @RequestPart(value = "dto") @Validated productCreateRequest: ProductCreateRequest
    ): ResponseEntity<MessageDTO> {
        productApplicationService.updateProduct(productId, mainImage, subImages, productCreateRequest)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "상품 기본 정보가 성공적으로 변경되었습니다."
        )
        return ResponseEntity(messageDTO, HttpStatus.OK)
    }

    // 상품 옵션&아이템 변경
    @PutMapping("/{productId}/options")
    @Operation(summary = "상품 옵션&아이템 변경", description = "상품 ID를 사용하여 해당 상품의 옵션과 아이템을 변경합니다.")
    fun updateProductOptions(
        @PathVariable productId: Long,
        @RequestBody productCreateRequest: ProductCreateRequest
    ): ResponseEntity<MessageDTO> {
        productApplicationService.updateProductOptions(productId, productCreateRequest)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "상품 옵션&아이템이 성공적으로 변경되었습니다."
        )
        return ResponseEntity(messageDTO, HttpStatus.OK)
    }

     */
}