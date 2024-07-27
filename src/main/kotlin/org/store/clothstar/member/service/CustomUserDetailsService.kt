package org.store.clothstar.member.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.store.clothstar.member.domain.CustomUserDetails
import org.store.clothstar.member.repository.AccountRepository


class CustomUserDetailsService(
    private val accountRepository: AccountRepository,
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return accountRepository.findByEmailOrNull(email)?.let { account -> CustomUserDetails(account) }
            ?: throw IllegalStateException("해당 아이디를 찾을 수 없습니다.")
    }
}