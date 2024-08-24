package org.store.clothstar.kakaoLogin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.kakaoLogin.dto.KakaoTokenResponseDto
import org.store.clothstar.kakaoLogin.service.KakaoLoginService

@RestController
class KakaoCallbackController(
    private val kakaoLoginService: KakaoLoginService,
) {
    @GetMapping("/auth/kakao/callback")
    fun kakaoCallback(@RequestParam code: String): ResponseEntity<KakaoTokenResponseDto> {
        val accessToken = kakaoLoginService.getAccessToken(code)
        return ResponseEntity.ok(accessToken)
    }
}