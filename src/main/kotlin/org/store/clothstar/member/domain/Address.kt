package org.store.clothstar.member.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.member.domain.vo.AddressInfo

@Entity(name = "address")
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val addressId: Long? = null,
    val receiverName: String,
    val telNo: String,
    val memberId: Long,
    val deliveryRequest: String,

    @Embedded
    val addressInfo: AddressInfo,
) : BaseEntity()