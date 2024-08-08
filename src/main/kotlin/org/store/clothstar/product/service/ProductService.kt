package org.store.clothstar.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.OptionValue
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.ProductOption
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.dto.response.ProductResponse
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
    @Transactional(readOnly = true)
    fun getProduct(productId: Long): ProductResponse {
        return productRepository.findByIdOrNull(productId)?.let { product ->
            ProductResponse.from(product)
        } ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."
        )
    }

    @Transactional
    fun createProduct(@Validated @RequestBody productCreateRequest: ProductCreateRequest): Long {
        //product 객체 생성후 저장
        val product = Product(
            memberId = productCreateRequest.memberId,
            categoryId = productCreateRequest.categoryId,
            name = productCreateRequest.name,
            content = productCreateRequest.content,
            price = productCreateRequest.price,
            displayStatus = productCreateRequest.displayStatus,
            saleStatus = productCreateRequest.saleStatus,
        )

        productRepository.save(product)

        //productOption 객체 생성후 저장
        //option -> 색상, optionsValues -> [빨강, 파랑]
        val options = productCreateRequest.productOptions
        for (option in options) {
            val productOption = ProductOption(
                optionName = option.optionName,
                optionOrderNo = option.optionOrderNo,
                product = product,
            )

            productOptionRepository.save(productOption)

            for (optionValueData in option.optionValues) {
                val optionValue = OptionValue(
                    productOption = productOption,
                    productColor = optionValueData.productColor,
                    value = optionValueData.value
                )

                optionValueRepository.save(optionValue)
            }
        }

        //item , item attribute 만들어야함 어떤것들 조합해서 나왔는지?
        //product
        val items = productCreateRequest.items
        for (itemData in items) {
            val item = Item(
                name = itemData.name,
                price = itemData.price,
                stock = itemData.stock,
                saleStatus = itemData.saleStatus,
                displayStatus = itemData.displayStatus,
                attributes = itemData.attributes,
                product = itemData.product
            )

            itemRepository.save(item)
        }

        return product.productId!!
    }

    @Transactional
    fun deleteProduct(productId: Long) {
        productRepository.deleteById(productId)
    }

    fun findByIdIn(productIds: List<Long>): List<Product> {
        return productRepository.findByIdIn(productIds).map {
            it ?: throw IllegalArgumentException("상품을 조회할 수 없습니다.")
        }
    }
}