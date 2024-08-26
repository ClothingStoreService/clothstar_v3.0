package org.store.clothstar.kakaoLogin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.kakaoLogin.dto.KakaoUserInfoResponseDto
import org.store.clothstar.kakaoLogin.service.KakaoLoginService

@RestController
class KakaoController(
    private val kakaoLoginService: KakaoLoginService,
) {
    @GetMapping("/auth/kakao/callback")
    fun kakaoCallback(@RequestParam code: String): ResponseEntity<KakaoUserInfoResponseDto> {
        // 액세스 토큰 받아오기
        val accessToken = kakaoLoginService.getAccessToken(code)
        // 사용자 정보 받아오기
        val userInfo = kakaoLoginService.getUserInfo(accessToken.accessToken!!)
        return ResponseEntity.ok(userInfo)
    }
}