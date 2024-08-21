package org.store.clothstar.kakaoLogin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KakaoCallbackController {
    @GetMapping("/auth/kakao/callback")
    fun kakaoCallback(@RequestParam code: String): ResponseEntity<String> {
        return ResponseEntity.ok("Authorization code: $code")
    }
}