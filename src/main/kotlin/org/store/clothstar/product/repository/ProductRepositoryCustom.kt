package org.store.clothstar.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository
import org.store.clothstar.product.domain.Product

@Repository
interface ProductRepositoryCustom {

    fun getProductLists(pageable: Pageable?): Page<Product?>?

    fun findAllOffsetPaging(pageable: Pageable): Page<Product>

    fun findAllSlicePaging(pageable: Pageable): List<Product>

    fun findEntitiesByCategoryWithOffsetPaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String,
    ): Page<Product>

    fun findEntitiesByCategoryWithSlicePaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String,
    ): Slice<Product>
}