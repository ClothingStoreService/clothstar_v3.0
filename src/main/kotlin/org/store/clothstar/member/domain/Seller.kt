package org.store.clothstar.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.store.clothstar.common.entity.BaseEntity

@Entity(name = "seller")
class Seller(
    @Id
    var memberId: Long,

    @Column(unique = true)
    val brandName: String,

    @Column(unique = true)
    val bizNo: String,
    val totalSellPrice: Long,
) : BaseEntity() {
}