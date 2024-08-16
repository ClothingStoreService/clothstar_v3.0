package org.store.clothstar.product.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.member.dto.response.SellerSimpleResponse
import org.store.clothstar.member.service.SellerService
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.dto.response.ProductDetailResponse
import org.store.clothstar.product.dto.response.ProductResponse
import org.store.clothstar.product.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val sellerService: SellerService,
) {
    @Transactional
    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    @Transactional(readOnly = true)
    fun getProductDetails(productId: Long): ProductResponse {
        val product = productRepository.findByIdOrNull(productId)
            ?: throw EntityNotFoundException("상품을 찾을 수 없습니다.")

        return ProductResponse.from(product)
    }

    @Transactional
    fun getProductLinesByCategoryWithOffsetPaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String?,
    ): Page<ProductDetailResponse> {
        val allOffsetPagingByCategory =
            productRepository.findEntitiesByCategoryWithOffsetPaging(categoryId, pageable, keyword)

        return allOffsetPagingByCategory.map { product ->
            val seller = sellerService.getSellerById(product.memberId)
            val sellerSimpleResponse = SellerSimpleResponse.getSellerSimpleResponseBySeller(seller)
            ProductDetailResponse.getProductDetailResponseByProduct(product, sellerSimpleResponse)
        }
    }

    @Transactional
    fun getProductLinesByCategoryWithSlicePaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String?,
    ): Slice<ProductDetailResponse> {
        val allSlicePagingByCategory =
            productRepository.findEntitiesByCategoryWithSlicePaging(categoryId, pageable, keyword)

        return allSlicePagingByCategory.map { product ->
            val seller = sellerService.getSellerById(product.memberId)
            val sellerSimpleResponse = SellerSimpleResponse.getSellerSimpleResponseBySeller(seller)
            ProductDetailResponse.getProductDetailResponseByProduct(product, sellerSimpleResponse)
        }
    }

    fun findByProductIdIn(productIds: List<Long>): List<Product> {
        return productRepository.findByProductIdIn(productIds).map {
            it ?: throw IllegalArgumentException("상품을 조회할 수 없습니다.")
        }
    }

    fun getProductById(productId: Long): Product {
        return productRepository.findByIdOrNull(productId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.")
    }
}