package org.store.clothstar.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?

    @Query("select acc from account acc where acc.userId = :userId and acc.role = :role")
    fun findByUserIdAndRole(userId: Long, role: MemberRole): Account?
}