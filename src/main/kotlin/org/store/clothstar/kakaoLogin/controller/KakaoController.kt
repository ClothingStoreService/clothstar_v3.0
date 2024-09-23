package org.store.clothstar.kakaoLogin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.kakaoLogin.service.KakaoLoginService

@RestController
class KakaoController(
    private val kakaoLoginService: KakaoLoginService,
) {
    // 인가코드 받기
    @GetMapping("/auth/kakao/callback")
    fun kakaoCallback(@RequestParam code: String): ResponseEntity<String> {
        return ResponseEntity.ok(code)
    }
}