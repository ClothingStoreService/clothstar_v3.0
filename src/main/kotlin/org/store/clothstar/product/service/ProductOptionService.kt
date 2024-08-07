package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.store.clothstar.product.domain.entity.Product
import org.store.clothstar.product.domain.entity.ProductOption
import org.store.clothstar.product.repository.ProductOptionRepository

@Service
class ProductOptionService (
    val productOptionRepository: ProductOptionRepository
) {
    fun createProductOption(product: Product) {
        // create product option
    }
}