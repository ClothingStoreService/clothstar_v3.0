package org.store.clothstar.common.config.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.common.dto.AccessTokenResponse
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.RefreshTokenInValidException
import org.store.clothstar.common.error.exception.RefreshTokenNotFoundException

@Tag(name = "Auth")
@RestController
class JwtController(
    private val jwtUtil: JwtUtil,
    private val jwtService: JwtService,
) {
    private val log = KotlinLogging.logger {}

    @Operation(summary = "access 토큰 재발급", description = "refresh 토큰으로 access 토큰을 재발급 한다.")
    @PostMapping("/v1/access")
    fun reissue(@CookieValue(name = "refreshToken") refreshToken: String?): ResponseEntity<AccessTokenResponse> {
        log.info { "access 토큰 refresh 요청: ${refreshToken}" }

        refreshToken?.let {
            refreshTokenValidCheck(refreshToken)
        }

        val accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken!!)
        log.info { "access 토큰이 갱신 되었습니다. ${accessToken}" }

        val accessTokenResponse = AccessTokenResponse(
            accessToken = accessToken,
            message = "access 토큰이 생성 되었습니다.",
            success = true,
        )

        return ResponseEntity.ok<AccessTokenResponse>(accessTokenResponse)
    }

    private fun refreshTokenValidCheck(refreshToken: String?) {
        if (refreshToken == null) {
            log.info { "refresh 토큰이 없습니다." }
            throw RefreshTokenNotFoundException(ErrorCode.NOT_FOUND_REFRESH_TOKEN)
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            log.info { "refresh 토큰이 만료되었거나 유효하지 않습니다." }
            throw RefreshTokenInValidException(ErrorCode.INVALID_REFRESH_TOKEN)
        }
    }
}