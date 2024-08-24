package org.store.clothstar.kakaoLogin.dto

import com.fasterxml.jackson.annotation.JsonProperty



class KakaoTokenResponseDto {
    @JsonProperty("token_type")
    private val tokenType: String? = null

    @JsonProperty("access_token")
    val accessToken: String? = null

    /**
     * 액세스 토큰과 ID 토큰의 만료 시간(초)
     */
    @JsonProperty("expires_in")
    private val expiresIn: Int? = null

    @JsonProperty("refresh_token")
    private val refreshToken: String? = null

    /**
     * 리프레시 토큰 만료 시간(초)
     */
    @JsonProperty("refresh_token_expires_in")
    private val refreshTokenExpiresIn: Int? = null

    @JsonProperty("id_token")
    private val idToken: String? = null

    @JsonProperty("scope")
    private val scope: String? = null
}