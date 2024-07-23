package org.store.clothstar.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.member.domain.Account
import java.util.*

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Optional<Account>
}