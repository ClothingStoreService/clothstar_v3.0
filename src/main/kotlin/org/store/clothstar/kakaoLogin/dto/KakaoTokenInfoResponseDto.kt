package org.store.clothstar.kakaoLogin.dto

import com.fasterxml.jackson.annotation.JsonProperty

class KakaoTokenInfoResponseDto {
    // 액세스 토큰 만료 시간(밀리초)
    @JsonProperty("expiresInMillis")
    private val expiresInMillis: Int? = null

    // 회원번호
    @JsonProperty("id")
    private val id: String? = null

    // 액세스 토큰 만료 시간(초)
    @JsonProperty("expires_in")
    val expiresIn: Int? = null

    // 앱 아이디
    @JsonProperty("app_id")
    val appId: Int? = null

    // 앱 아이디
    @JsonProperty("appId")
    val appIdd: Int? = null
}