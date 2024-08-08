package org.store.clothstar.member.authentication.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.store.clothstar.member.domain.Account

class CustomUserDetails(
    val account: Account,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_" + account.role))
    }

    override fun getPassword(): String {
        return account.password
    }

    override fun getUsername(): String {
        return account.email
    }

    override fun isAccountNonExpired(): Boolean {
        //true -> 계정 만료되지 않았음
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        //true -> 계정 잠금되지 않음
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        //true -> 패스워드 만료 되지 않음
        return true
    }

    override fun isEnabled(): Boolean {
        //ture -> 계정 사용 가능
        return true
    }
}