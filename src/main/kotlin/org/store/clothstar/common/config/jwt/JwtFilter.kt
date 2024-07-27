package org.store.clothstar.common.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.member.domain.CustomUserDetails
import org.store.clothstar.member.dto.request.MemberLoginRequest

class JwtFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
) : UsernamePasswordAuthenticationFilter() {
    init {
        setFilterProcessesUrl("/v1/members/login")
    }

    private val log = LoggerFactory.getLogger(javaClass)

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        log.info("로그인 진행")

        try {
            val memberLoginRequest = ObjectMapper().readValue(request.inputStream, MemberLoginRequest::class.java)
            val email = memberLoginRequest.email
            val password = memberLoginRequest.password

            val authTokenDTO = UsernamePasswordAuthenticationToken(email, password, null)

            return authenticationManager.authenticate(authTokenDTO)
        } catch (ex: IOException) {
            throw RuntimeException(ex)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        log.info("로그인 성공")
        val customUserDetails = authentication.principal as CustomUserDetails

        val account = customUserDetails.account
        val accessToken = jwtUtil.createAccessToken(account)
        log.info("생성 accessToken: Bearer {}", accessToken)
        val refreshToken = jwtUtil.createRefreshToken(account)
        log.info("생성 refreshToken: Bearer {}", refreshToken)

        response.addHeader("Authorization", "Bearer ${accessToken}")
        response.addCookie(jwtUtil.createCookie("refreshToken", refreshToken))
        response.status = HttpStatus.OK.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"

        val messageDTO = MessageDTO(
            HttpServletResponse.SC_OK,
            "로그인 성공 하였습니다."
        )

        response.writer.print(ObjectMapper().writeValueAsString(messageDTO))
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        log.info("로그인 실패")

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"

        val messageDTO = MessageDTO(
            HttpServletResponse.SC_UNAUTHORIZED,
            errorMessage(failed)
        )

        response.writer.print(ObjectMapper().writeValueAsString(messageDTO))
    }

    private fun errorMessage(failed: AuthenticationException): String? {

        return if (failed is BadCredentialsException) {
            "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요."
        } else if (failed is DisabledException) {
            "계정이 비활성화 되어있습니다. 이메일 인증을 완료해주세요"
        } else {
            null
        }
    }
}