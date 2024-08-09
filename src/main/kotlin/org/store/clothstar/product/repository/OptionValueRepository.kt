package org.store.clothstar.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.product.domain.OptionValue
import org.store.clothstar.product.domain.ProductOption

interface OptionValueRepository : JpaRepository<OptionValue, Int> {
    fun findByProductOptionAndValue(productOption: ProductOption, value: String): OptionValue?
}