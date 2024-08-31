package org.store.clothstar.kakaoLogin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.kakaoLogin.dto.KakaoRenewTokenResponseDto
import org.store.clothstar.kakaoLogin.dto.KakaoTokenInfoResponseDto
import org.store.clothstar.kakaoLogin.dto.KakaoUserInfoResponseDto
import org.store.clothstar.kakaoLogin.dto.TokenUserInfoResponseDto
import org.store.clothstar.kakaoLogin.service.KakaoLoginService

@RestController
class KakaoController(
    private val kakaoLoginService: KakaoLoginService,
) {
    // 인가코드 받기 - 액세스 토큰 받기 - 사용자 정보 받기
    @GetMapping("/auth/kakao/callback")
    fun kakaoCallback(@RequestParam code: String): ResponseEntity<String> {
        // 액세스 토큰 받아오기 - 저장 나중에 하기
//        val accessToken = kakaoLoginService.getAccessToken(code)
//        // 사용자 정보 받아오기 - 저장 나중에 하기
//        val userInfo = kakaoLoginService.getUserInfo(accessToken.accessToken!!)
//        // 두 정보를 KakaoTokenUserInfoResponseDto로 합쳐서 반환
//        val tokenUserInfo = TokenUserInfoResponseDto(
//            accessToken = accessToken,
//            userInfo = userInfo
//        )
        return ResponseEntity.ok(code)
    }

    // 토큰 정보 확인
    @GetMapping("/auth/kakao/accessInfo")
    fun getAccessInfo(@RequestParam accessToken: String): KakaoTokenInfoResponseDto {
        val tokenInfo = kakaoLoginService.getTokenInfo(accessToken)
        return tokenInfo
    }

    // 액세스 토큰 갱신 - 유효성 검사 후 토큰 유효기간 만료 시 재로그인 요청
    @GetMapping("/auth/kakao/check")
    fun checkToken(@RequestParam accessToken: String, @RequestParam refreshToken: String): ResponseEntity<KakaoRenewTokenResponseDto> {
        val renewTokenInfo: KakaoRenewTokenResponseDto = kakaoLoginService.validateToken(accessToken, refreshToken)
        return ResponseEntity.ok(renewTokenInfo)
    }
}