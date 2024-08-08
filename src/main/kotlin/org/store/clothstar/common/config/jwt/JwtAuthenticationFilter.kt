package org.store.clothstar.common.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.store.clothstar.member.authentication.domain.CustomUserDetails
import org.store.clothstar.member.repository.AccountRepository

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val accountRepository: AccountRepository,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtUtil.resolveToken(request)
        log.info("doFilterInternal() 실행, token={}", token)

        if (token == null) {
            log.info("JWT 토큰정보가 없습니다.")
        } else if (!jwtUtil.validateToken(token)) {
            log.info("JWT 토큰이 만료되거나 잘못되었습니다.")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
        } else {
            authenticateUserWithToken(token)
        }

        filterChain.doFilter(request, response)
    }

    private fun authenticateUserWithToken(token: String) {
        val accountId = jwtUtil.getAccountId(token)
        log.info("refresh 토큰 memberId: {}", accountId)

        val account = accountRepository.findByAccountId(accountId)
            ?: throw IllegalStateException("해당 아이디를 찾을 수 없습니다.")

        val customUserDetails = CustomUserDetails(account)

        val authToken = UsernamePasswordAuthenticationToken(
            customUserDetails, null, customUserDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = authToken
    }
}