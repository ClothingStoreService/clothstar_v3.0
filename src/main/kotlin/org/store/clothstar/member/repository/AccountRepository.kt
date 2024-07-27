package org.store.clothstar.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.member.domain.Account

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmailOrNull(email: String?): Account?
}