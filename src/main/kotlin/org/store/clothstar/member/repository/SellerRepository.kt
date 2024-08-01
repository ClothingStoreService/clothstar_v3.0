package org.store.clothstar.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.member.domain.Seller

interface SellerRepository : JpaRepository<Seller, Long> {
    fun findByBizNo(bizNo: String): Seller?

    fun findByBrandName(brandName: String): Seller?
}