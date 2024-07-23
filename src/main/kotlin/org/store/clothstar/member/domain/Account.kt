package org.store.clothstar.member.domain

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val accountId: Long? = null,

    @Column(unique = true)
    var email: String,
    var password: String
) {
//    constructor(email: String, password: String): this(email, password) {
//        this.email = email,
//        this.password = password
//    }

    fun toAccount() {

    }

    fun updatedPassword(password: String) {
        this.password = password
    }
}