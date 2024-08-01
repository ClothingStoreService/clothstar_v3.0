package org.store.clothstar.member.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.member.domain.vo.AddressInfo

@Entity(name = "address")
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val addressId: Long,
    private val receiverName: String,
    private val telNo: String,
    private val defaultAddress: Boolean,

    @Embedded
    val addressInfo: AddressInfo,

    @ManyToOne
    @JoinColumn(name = "member_id")
    private val member: Member
) : BaseEntity() {
}