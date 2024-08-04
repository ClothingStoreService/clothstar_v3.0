package org.store.clothstar.common.config.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties(
    val secretKey: String,
    val accessTokenValidTimeMillis: Long,
    val refreshTokenValidTimeMillis: Long,
) {
}