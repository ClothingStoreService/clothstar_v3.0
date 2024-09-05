package org.store.clothstar.product.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.product.dto.response.ProductListResponse
import org.store.clothstar.product.dto.response.ProductResponse


@Service
class ProductApplicationService(
    private val productService: ProductService,
    private val productOptionService: ProductOptionService,
    private val itemService: ItemService,
    private val memberService: MemberService
) {

    @Transactional(readOnly = true)
    fun getProductDetails(productId: Long, isSeller: Boolean): ProductResponse {
        return productService.getProductDetails(productId, isSeller)
    }

    @Transactional(readOnly = true)
    fun getAllProductsOffsetPaging(pageable: Pageable, keyword: String?): Page<ProductListResponse> {
        return productService.getAllProductsOffsetPaging(pageable, keyword)
    }

    @Transactional(readOnly = true)
    fun getAllProductsSlicePaging(pageable: Pageable, keyword: String?): Slice<ProductListResponse> {
        return productService.getAllProductsSlicePaging(pageable, keyword)
    }
}