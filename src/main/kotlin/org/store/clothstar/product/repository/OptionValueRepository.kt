package org.store.clothstar.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.product.domain.entity.OptionValue

interface OptionValueRepository : JpaRepository<OptionValue, Int> {

}