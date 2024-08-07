package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    fun findByProductIdIn(productIds: List<Long>): List<Product> {
        return productRepository.findByProductIdIn(productIds).map {
            it ?: throw IllegalArgumentException("상품을 조회할 수 없습니다.")
        }
    }
    
    fun Product createProduct(product: Product): Product {
        // create product
        val product = productRepository.save(product)
        return product
    }
}