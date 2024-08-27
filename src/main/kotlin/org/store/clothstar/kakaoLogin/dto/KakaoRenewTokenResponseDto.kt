package org.store.clothstar.kakaoLogin.dto

import com.fasterxml.jackson.annotation.JsonProperty

class KakaoRenewTokenResponseDto {
    // 토큰 타입, bearer로 고정
    @JsonProperty("token_type") private val tokenType: String? = null
    // 갱신된 사용자 액세스 토큰 값
    @JsonProperty("access_token") val accessToken: String? = null
    // 액세스 토큰 만료 시간(초)
    @JsonProperty("expires_in") val expiresIn: Int? = null
}