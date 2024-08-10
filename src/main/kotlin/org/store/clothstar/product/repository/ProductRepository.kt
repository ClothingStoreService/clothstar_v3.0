package org.store.clothstar.product.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.store.clothstar.product.domain.Product
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByProductIdIn(productLineIds: List<Long>): List<Product?>

    @EntityGraph(attributePaths = ["productOptions", "items", "imageList"])
    fun findWithDetailsByProductId(productId: Long): Optional<Product>
}