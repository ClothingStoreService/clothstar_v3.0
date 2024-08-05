package org.store.clothstar.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.store.clothstar.common.config.jwt.JwtUtil
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.member.domain.CustomUserDetails
import org.store.clothstar.member.dto.request.MemberLoginRequest
import java.io.IOException

class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
) : UsernamePasswordAuthenticationFilter() {
    init {
        setFilterProcessesUrl("/v1/members/login")
    }

    private val log = KotlinLogging.logger {}

    /**
     * 로그인 창에서 입력한 id, password를 받아서
     * Authentication Manager에 던져 줘야 하는데 그 DTO역할을 하는 객체가 UsernamePasswordAuthenticationToken이다.
     * Authentication Manager에 전달하면 최종적으로 Authentication에 전달 된다.
     * return 하면 Authentication Manager에 던져진다.
     *
     *
     * AuthenticationManager에 던지기 위해서 주입을 받아야 한다.
     */
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        log.info { "로그인 진행" }

        try {
            val memberLoginRequest = ObjectMapper().readValue(request.inputStream, MemberLoginRequest::class.java)
            log.info { "login parameter memberLoginRequest: ${memberLoginRequest.toString()}" }

            val email = memberLoginRequest.email
            val password = memberLoginRequest.password

            val authTokenDTO = UsernamePasswordAuthenticationToken(email, password, null)
            return authenticationManager.authenticate(authTokenDTO)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication,
    ) {
        log.info { "로그인 성공" }
        val customUserDetails = authentication.principal as CustomUserDetails

        val account = customUserDetails.account
        log.info { "account: ${account.toString()}" }

        val accessToken = jwtUtil.createAccessToken(account)
        log.info { "생성 accessToken: Bearer $accessToken " }

        val refreshToken = jwtUtil.createRefreshToken(account)
        log.info { "생성 refreshToken: Bearer $refreshToken" }

        response.addHeader("Authorization", "Bearer $accessToken")
        response.addCookie(jwtUtil.createCookie("refreshToken", refreshToken))
        response.status = HttpStatus.OK.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"

        val messageDTO: MessageDTO = MessageDTO(
            HttpServletResponse.SC_OK,
            "로그인 성공 하였습니다."
        )

        response.writer.print(ObjectMapper().writeValueAsString(messageDTO))
    }

    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException,
    ) {
        log.info { "로그인 실패" }

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"

        val messageDTO = MessageDTO(
            HttpServletResponse.SC_UNAUTHORIZED,
            errorMessage(failed),
        )

        response.writer.print(ObjectMapper().writeValueAsString(messageDTO))
    }

    private fun errorMessage(failed: AuthenticationException): String {
        return if (failed is BadCredentialsException) {
            "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요."
        } else if (failed is DisabledException) {
            "계정이 비활성화 되어있습니다. 이메일 인증을 완료해주세요"
        } else {
            "로그인 성공"
        }
    }
}
