package org.store.clothstar.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.dto.response.ProductResponse
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.ProductRepository

@Service
class ItemService(
    private val itemRepository: ItemRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional(readOnly = true)
    fun getProduct(productId: Long): ProductResponse {
        return productRepository.findByIdOrNull(productId)?.let { product ->
            ProductResponse.from(product)
        } ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."
        )
    }


    fun findByIdIn(productIds: List<Long>): List<Item> {
        return itemRepository.findByIdIn(productIds).map {
            it ?: throw IllegalArgumentException("상품을 찾을수 없습니다.")
        }
    }
}