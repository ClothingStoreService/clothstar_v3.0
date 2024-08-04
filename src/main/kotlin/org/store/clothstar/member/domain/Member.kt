package org.store.clothstar.member.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.ModifyNameRequest

@Entity(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long? = null,

    @Column(unique = true)
    val telNo: String,
    var name: String,

    @Embedded
    val memberShoppingActivity: MemberShoppingActivity
) : BaseEntity() {
    fun updateName(modifyNameRequest: ModifyNameRequest) {
        this.name = modifyNameRequest.name
    }
}