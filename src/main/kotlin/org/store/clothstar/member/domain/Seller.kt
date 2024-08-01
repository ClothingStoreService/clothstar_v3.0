package org.store.clothstar.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.store.clothstar.common.entity.BaseEntity

@Entity(name = "seller")
class Seller(
    @Id
    private var memberId: Long,

    @Column(unique = true)
    private val brandName: String,

    @Column(unique = true)
    private val bizNo: String,
    private val totalSellPrice: Long
) : BaseEntity() {
}