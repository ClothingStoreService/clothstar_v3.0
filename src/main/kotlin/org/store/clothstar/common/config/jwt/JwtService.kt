package org.store.clothstar.common.config.jwt

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository
import java.util.*

class JwtService(
    private val jwtUtil: JwtUtil,
    private val memberRepository: MemberRepository,
    private val accountRepository: AccountRepository,
) {
    fun getRefreshToken(request: HttpServletRequest): String? {
        if (request.cookies == null) {
            return null
        }

        return Arrays.stream(request.cookies)
            .filter { cookie: Cookie -> cookie.name == "refreshToken" }
            .findFirst()
            .map { cookie: Cookie -> cookie.value }
            .orElse(null)
    }

    fun getAccessTokenByRefreshToken(refreshToken: String): String {
        val accountId = jwtUtil.getAccountId(refreshToken)

        val account = accountRepository.findByAccountId(accountId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_ACCOUNT)

        return jwtUtil.createAccessToken(account)
    }
}
