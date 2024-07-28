package org.store.clothstar.member.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity

@Entity(name = "account")
class Account(
    @Column(unique = true)
    val email: String, //getter 자동 생성
    var password: String,

    @Enumerated(EnumType.STRING)
    val role: MemberRole,

    //default 파라미터는 마지막에 있는것이 관례
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId: Long? = null,
) : BaseEntity() {
    fun updatedPassword(password: String) {
        this.password = password
    }
}