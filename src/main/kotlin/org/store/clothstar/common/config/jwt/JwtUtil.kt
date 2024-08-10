package org.store.clothstar.common.config.jwt

import io.jsonwebtoken.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.store.clothstar.member.domain.Account
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil(
    private val jwtProperties: JwtProperties,
) {
    companion object {
        private val AUTHORIZATION_HEADER: String = "Authorization"
        private val TOKEN_PREFIX: String = "Bearer "
        private val ACCESS_TOKEN: String = "ACCESS_TOKEN"
        private val REFRESH_TOKEN: String = "REFRESH_TOKEN"
    }

    private val log = LoggerFactory.getLogger(javaClass)

    val secretKey: SecretKey = SecretKeySpec(
        jwtProperties.secretKey.toByteArray(),
        Jwts.SIG.HS256.key().build().getAlgorithm()
    )

    //http 헤더 Authorization의 값이 jwt token인지 확인하고 token값을 넘기는 메서드
    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length)
        }

        return null
    }

    fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 60 * 30
        cookie.isHttpOnly = true

        return cookie
    }

    fun createAccessToken(account: Account): String {
        return createToken(account, jwtProperties.accessTokenValidTimeMillis, ACCESS_TOKEN)
    }

    fun createRefreshToken(account: Account): String {
        return createToken(account, jwtProperties.refreshTokenValidTimeMillis, REFRESH_TOKEN)
    }

    private fun createToken(account: Account, tokenValidTimeMillis: Long, tokenType: String): String {
        val accountId = account.accountId
        val currentDate = Date()
        val expireDate = Date(currentDate.time + tokenValidTimeMillis)

        val jwtHeader = Jwts.header()
            .type("JWT")
            .build()

        return Jwts.builder()
            .header().add(jwtHeader).and()
            .issuedAt(currentDate)
            .expiration(expireDate)
            .claim("tokenType", tokenType)
            .claim("id", accountId)
            .claim("role", account.role)
            .signWith(secretKey)
            .compact();
    }

    fun getClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getAccountId(token: String): Long {
        val claims = getClaims(token)
        return claims.get("id", Integer::class.java).toLong()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (ex: MalformedJwtException) {
            log.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            log.error("Expired JWT token");
        } catch (ex: UnsupportedJwtException) {
            log.error("Unsupported JWT token");
        } catch (ex: IllegalArgumentException) {
            log.error("JWT claims string is empty.");
        }

        return false
    }
}