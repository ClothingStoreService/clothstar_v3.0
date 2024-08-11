package org.store.clothstar.product.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.dto.response.ProductResponse
import org.store.clothstar.product.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    @Transactional(readOnly = true)
    fun getProductDetails(productId: Long): ProductResponse {
        val product = productRepository.findWithDetailsByProductId(productId)
            .orElseThrow { EntityNotFoundException("상품을 찾을 수 없습니다.") }

        return ProductResponse.from(product)
    }

    fun findByProductIdIn(productIds: List<Long>): List<Product> {
        return productRepository.findByProductIdIn(productIds).map {
            it ?: throw IllegalArgumentException("상품을 조회할 수 없습니다.")
        }
    }
}