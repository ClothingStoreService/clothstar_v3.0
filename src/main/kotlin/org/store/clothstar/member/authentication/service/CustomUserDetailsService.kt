package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.member.authentication.domain.CustomUserDetails
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.repository.AccountRepository

@Service
class CustomUserDetailsService(
    private val accountRepository: AccountRepository,
) : UserDetailsService {
    private val log = KotlinLogging.logger {}

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        log.info { "loadUserByUsername() 실행" }
        val account: Account = accountRepository.findByEmail(email)
            ?: throw UsernameNotFoundException(ErrorCode.NOT_FOUND_ACCOUNT.message)

        return CustomUserDetails(account)
    }
}