package org.store.clothstar.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.product.domain.entity.ProductOption

interface ProductOptionRepository : JpaRepository<ProductOption, Long> {

}