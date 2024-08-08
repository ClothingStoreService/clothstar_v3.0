package org.store.clothstar.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.product.domain.Item

interface ItemRepository : JpaRepository<Item, Long> {
    fun findAllByProductId(productId: Long?): List<Item?>

    fun findByIdIn(productIds: List<Long>): List<Item?>
}