package org.store.clothstar.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.product.domain.ProductOption

interface ProductOptionRepository : JpaRepository<ProductOption, Long> {

}