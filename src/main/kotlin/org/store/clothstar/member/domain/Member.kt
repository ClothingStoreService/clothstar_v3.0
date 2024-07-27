package org.store.clothstar.member.domain

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.ModifyNameRequest

@Entity(name = "member")
class Member(
    @Id
    val memberId: Long,

    val telNo: String,
    var name: String,

    @Embedded
    val memberShoppingActivity: MemberShoppingActivity
) : BaseEntity() {
    fun updateName(modifyNameRequest: ModifyNameRequest) {
        this.name = modifyNameRequest.name
    }
}