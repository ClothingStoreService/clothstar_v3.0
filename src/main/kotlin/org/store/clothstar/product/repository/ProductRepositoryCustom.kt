package org.store.clothstar.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.store.clothstar.product.domain.Product

@Repository
interface ProductRepositoryCustom {

    fun getProductLists(pageable: Pageable?): Page<Product?>?

    fun findWithDetailsByProductId(productId: Long): Product?

    fun findAllOffsetPaging(pageable: Pageable): Page<Product>

    fun findAllSlicePaging(pageable: Pageable): List<Product>




}