package org.store.clothstar.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.store.clothstar.common.dto.MessageDTO
import java.io.IOException

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val log = KotlinLogging.logger {}

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        log.error { "인증 실패 로직 실행" }
        
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"

        val messageDTO = MessageDTO(
            HttpServletResponse.SC_UNAUTHORIZED,
            "권한이 없습니다."
        )

        response.writer.write(ObjectMapper().writeValueAsString(messageDTO))
    }
}
