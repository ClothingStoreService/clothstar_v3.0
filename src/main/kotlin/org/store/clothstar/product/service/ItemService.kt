package org.store.clothstar.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.ItemAttribute
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.type.SaleStatus
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.dto.response.ItemResponse
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.OptionValueRepository
import org.store.clothstar.product.repository.ProductOptionRepository

@Service
class ItemService(
    private val itemRepository: ItemRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val optionValueRepository: OptionValueRepository
) {

    @Transactional(readOnly = true)
    fun getProduct(productId: Long): ItemResponse {
        return itemRepository.findByIdOrNull(productId)?.let { product ->
            ItemResponse.from(product)
        } ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."
        )
    }

    @Transactional
    fun createItem(product: Product, itemRequest: ProductCreateRequest.ItemCreateRequest): Item {
        // 옵션 값들을 조회
        val optionValues = itemRequest.optionAttributes.map { attr ->
            val productOption = productOptionRepository.findByProductAndOptionOrderNo(product, attr.optionOrderNo)
                ?: throw IllegalArgumentException("Invalid option order number: ${attr.optionOrderNo}")

            val optionValue = optionValueRepository.findByProductOptionAndValue(productOption, attr.optionValue)
                ?: throw IllegalArgumentException("Invalid option value: ${attr.optionValue}")

            optionValue
        }

        // 아이템 생성
        val item = Item(
            name = optionValues.joinToString("/") { it.value },
            finalPrice = itemRequest.price,
            stock = itemRequest.stock,
            saleStatus = SaleStatus.ON_SALE,
            displayStatus = itemRequest.displayStatus,
            product = product,
            attributes = optionValues.map { optionValue ->
                ItemAttribute(
                    optionId = optionValue.productOption.productOptionId!!,
                    name = optionValue.productOption.optionName,
                    value = optionValue.value,
                    valueId = optionValue.optionValueId!!
                )
            }.toMutableSet()
        )

        // 아이템 저장
        return itemRepository.save(item)
    }

    fun restoreProductStockByOrderDetail(orderDetail: OrderDetail) {
        val productEntity = itemRepository.findByIdOrNull(orderDetail.itemId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.")

        productEntity.restoreStock(orderDetail.quantity)
    }

    fun findByIdIn(productIds: List<Long>): List<Item> {
        return itemRepository.findByItemIdIn(productIds).map {
            it ?: throw IllegalArgumentException("상품을 찾을수 없습니다.")
        }
    }
}