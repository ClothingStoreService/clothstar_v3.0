package org.store.clothstar.member.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity

@Entity(name = "account")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId: Long? = null,

    @Column(unique = true)
    val email: String, //getter 자동 생성
    var password: String? = null,

    @Enumerated(EnumType.STRING)
    var role: MemberRole,

    val userId: Long,
) : BaseEntity() {
    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateRole(role: MemberRole) {
        this.role = role
    }

    override fun toString(): String {
        return "Account(accountId=$accountId, email='$email', password='$password', role=$role, userId=$userId)"
    }
}