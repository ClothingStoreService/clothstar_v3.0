package org.store.clothstar.kakaoLogin.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.store.clothstar.kakaoLogin.dto.KakaoTokenResponseDto
import org.store.clothstar.kakaoLogin.dto.KakaoUserInfoResponseDto

@EnableScheduling
@Service
class KakaoLoginService {

    private val logger = KotlinLogging.logger {}

    @Value("\${spring.security.oauth2.client.registration.kakao.client_id}")
    private lateinit var clientId: String

    @Value("\${spring.security.oauth2.client.registration.kakao.client_secret}")
    private lateinit var clientSecret: String

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect_uri}")
    private lateinit var redirectUri: String

    @Value("\${spring.security.oauth2.client.provider.kakao.token_uri}")
    lateinit var tokenUri: String

    @Value("\${spring.security.oauth2.client.provider.kakao.user_info_uri}")
    lateinit var userUri: String

    // 토큰 가져오기
    fun getAccessToken(code: String): KakaoTokenResponseDto {
        // 토큰 요청 데이터
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("code", code)
        params.add("client_secret", clientSecret)
        params.add("client_id", clientId)
        params.add("grant_type", "authorization_code")
        params.add("redirect_url", redirectUri)

        logger.info { "Requesting token with params: $params" }

        // 웹 클라이언트로 요청 보내기
        val response = WebClient.create(tokenUri)
            .post()
            .body(BodyInserters.fromFormData(params))
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info { "Token response: $response" }

        // json 응답을 객체로 변환
        val objectMapper = ObjectMapper()
        val kakaoToken: KakaoTokenResponseDto = objectMapper.readValue(response, KakaoTokenResponseDto::class.java)

        logger.info { "Access Token : ${kakaoToken.accessToken}" }
        return kakaoToken
    }

    // 사용자 정보 가져오기
    fun getUserInfo(accessToken: String): KakaoUserInfoResponseDto {
        // 웹 클라이언트로 요청 보내기
        val response = WebClient.create(userUri)
            .get()
            .header("Authorization", "Bearer $accessToken")
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info { "User info response: $response" }

        // json 응답을 객체로 변환
        val objectMapper = ObjectMapper()
        val userInfo = objectMapper.readValue(response, KakaoUserInfoResponseDto::class.java)

        logger.info { "email : ${userInfo.kakaoAccount!!.email}" }
        return userInfo
    }
}