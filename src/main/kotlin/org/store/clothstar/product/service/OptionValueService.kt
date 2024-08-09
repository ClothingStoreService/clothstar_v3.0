package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.product.domain.OptionValue
import org.store.clothstar.product.domain.ProductOption
import org.store.clothstar.product.repository.OptionValueRepository

@Service
class OptionValueService (
    private val optionValueRepository: OptionValueRepository
) {
    @Transactional
    fun createOptionValue(productOption: ProductOption, value: String): OptionValue {
        val optionValue = OptionValue(
            value = value,
            productOption = productOption
        )

        return optionValueRepository.save(optionValue)
    }
}