package org.store.clothstar.kakaoLogin.dto

class TokenUserInfoResponseDto(
    val accessToken: KakaoTokenResponseDto,
    val userInfo: KakaoUserInfoResponseDto
)