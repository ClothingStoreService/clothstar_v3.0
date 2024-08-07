package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.product.domain.OptionValue
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.ProductOption
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.repository.OptionValueRepository
import org.store.clothstar.product.repository.ProductOptionRepository

@Service
class ProductOptionService (
    private val productOptionRepository: ProductOptionRepository,
    private val optionValueRepository: OptionValueRepository
) {
    @Transactional
    fun createProductOption(product: Product, optionRequest: ProductCreateRequest.ProductOptionCreateRequest): ProductOption {
        val productOption = ProductOption(
            optionName = optionRequest.optionName,
            optionOrderNo = optionRequest.optionOrderNo,
            product = product
        )

        val savedProductOption = productOptionRepository.save(productOption)

        val optionValues = optionRequest.optionValues.split(",").map { value ->
            OptionValue(
                value = value.trim(),
                productOption = savedProductOption
            )
        }

        savedProductOption.optionValues.addAll(optionValues)
        optionValueRepository.saveAll(optionValues)

        return savedProductOption
    }
}