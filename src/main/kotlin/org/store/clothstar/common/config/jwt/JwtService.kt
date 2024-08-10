package org.store.clothstar.common.config.jwt

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.repository.AccountRepository

@Service
class JwtService(
    private val jwtUtil: JwtUtil,
    private val accountRepository: AccountRepository,
) {
    fun getAccessTokenByRefreshToken(refreshToken: String): String {
        val accountId = jwtUtil.getAccountId(refreshToken)

        val account = accountRepository.findByIdOrNull(accountId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_ACCOUNT)

        return jwtUtil.createAccessToken(account)
    }
}
