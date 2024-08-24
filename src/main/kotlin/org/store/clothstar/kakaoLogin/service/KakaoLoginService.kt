package org.store.clothstar.kakaoLogin.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.store.clothstar.kakaoLogin.dto.KakaoTokenResponseDto

@Service
class KakaoLoginService{

    private val logger = KotlinLogging.logger {}

    @Value("\${kakao.client_id}")
    private lateinit var clientId: String

    @Value("\${kakao.client_secret}")
    private lateinit var clientSecret: String

    @Value("\${kakao.redirect_uri}")
    private lateinit var redirectUri: String

    @Value("\${kakao.api.uri.base}")
    private lateinit var baseUri: String

    @Value("\${kakao.login.uri.token}")
    private lateinit var tokenUri: String

    fun getAccessToken(code:String): KakaoTokenResponseDto {
        // 토큰 요청 데이터 -> MultiValueMap
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("code", code)
        params.add("client_secret", clientSecret)
        params.add("client_id", clientId)
        params.add("grant_type", "authorization_code")
        params.add("redirect_url", redirectUri)

        // 웹 클라이언트로 요청 보내기
        val response = WebClient.create(baseUri)
            .post()
            .uri(tokenUri)
            .body(BodyInserters.fromFormData(params))
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        // json 응답을 객체로 변환
        val objectMapper = ObjectMapper()

        val kakaoToken: KakaoTokenResponseDto = objectMapper.readValue(response,KakaoTokenResponseDto::class.java)

        logger.info("Access Token : {}", kakaoToken.accessToken)

        return kakaoToken
    }
}