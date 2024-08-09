package org.store.clothstar.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.store.clothstar.common.util.URIBuilder.buildURI
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.dto.response.ItemResponse
import org.store.clothstar.product.service.ItemService
import java.net.URI

@Tag(name = "Products", description = "Products(상품 옵션) 관련 API 입니다.")
@RestController
class ItemController(
    private val itemService: ItemService,
) {
    @Operation(summary = "상품 옵션 상세 조회", description = "productId로 상품 옵션 한개를 상세 조회한다.")
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable("productId") itemId: Long): ResponseEntity<ItemResponse> {
        val productResponse = itemService.getProduct(itemId)
        return ResponseEntity.ok().body<ItemResponse>(productResponse)
    }
//
//    @Operation(summary = "상품 옵션 등록", description = "상품 옵션 이름, 추가금액, 재고 수를 입력하여 상품을 신규 등록한다.")
//    @PostMapping
//    fun createProduct(@Validated @RequestBody productCreateRequest: ProductCreateRequest): ResponseEntity<URI> {
//        val itemId = itemService.createItem(productCreateRequest)
//        val location = buildURI(itemId)
//
//        return ResponseEntity.created(location).build()
//    }
//
//    @Operation(summary = "상품 옵션 삭제", description = "상품 옵션 id로 상품 옵션을 삭제한다.")
//    @DeleteMapping("/{productId}")
//    fun deleteProduct(@PathVariable itemId: Long): ResponseEntity<Void> {
//        itemService.deleteItem(itemId)
//
//        return ResponseEntity.noContent().build()
//    }
}