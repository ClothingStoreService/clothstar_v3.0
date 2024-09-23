package org.store.clothstar.kakaoLogin.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenResponseDto @JsonCreator constructor(

    // 토큰 타입, bearer로 고정
    @JsonProperty("token_type")
    private val tokenType: String? = null,

    // 사용자 액세스 토큰 값
    @JsonProperty("access_token")
    val accessToken: String? = null,

    // 액세스 토큰과 ID 토큰의 만료 시간(초)
    @JsonProperty("expires_in")
    private val expiresIn: Int? = null,

    // 사용자 리프레시 토큰 값
    @JsonProperty("refresh_token")
    private val refreshToken: String? = null,

    // 리프레시 토큰 만료 시간(초)
    @JsonProperty("refresh_token_expires_in")
    private val refreshTokenExpiresIn: Int? = null,

    // 인증된 사용자의 정보 조회 권한 범위 / 범위가 여러 개일 경우, 공백으로 구분
    @JsonProperty("scope")
    private val scope: String? = null,
)