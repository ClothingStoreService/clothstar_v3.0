package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.product.domain.entity.Product
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.OptionValueRepository
import org.store.clothstar.product.repository.ProductOptionRepository
import org.store.clothstar.product.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val optionValueRepository: OptionValueRepository,
    private val itemRepository: ItemRepository,
    ) {

//    @Transactional
//    fun createProduct(productCreateRequest: ProductCreateRequest): Product {
//
//    }
}