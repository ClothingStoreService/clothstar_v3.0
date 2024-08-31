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
import org.store.clothstar.kakaoLogin.dto.*

@EnableScheduling
@Service
class KakaoLoginService {

    private val logger = KotlinLogging.logger {}

    @Value("\${kakao.client_id}")
    private lateinit var clientId: String

    @Value("\${kakao.client_secret}")
    private lateinit var clientSecret: String

    @Value("\${kakao.redirect_uri}")
    private lateinit var redirectUri: String

    @Value("\${kakao.login.uri.token}")
    private lateinit var tokenUri: String

    @Value("\${kakao.api.uri.user}")
    private lateinit var userUri: String

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
        val response = WebClient.create("https://kauth.kakao.com")
            .post()
            .uri(tokenUri)
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
        val response = WebClient.create("https://kapi.kakao.com")
            .get()
            .uri(userUri)
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

    // 토큰 유효성 검사
    fun validateToken(accessToken: String, refreshToken: String): KakaoRenewTokenResponseDto {
        return try {
            getTokenInfo(accessToken)
            // 액세스 토큰이 유효한 경우 토큰 갱신
            refreshAccessToken(refreshToken)
        } catch (e: Exception) {
            // getTokenInfo에서 예외가 발생한 경우 재로그인 필요
            throw Exception("Access Token이 만료되었습니다. 재로그인이 필요합니다.")
        }
    }

    // 토큰 정보 보기
    fun getTokenInfo(accessToken: String): KakaoTokenInfoResponseDto {
            val response = WebClient.create("https://kapi.kakao.com")
                .get()
                .uri("/v1/user/access_token_info")
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            val objectMapper = ObjectMapper()
            val tokenInfo: KakaoTokenInfoResponseDto =
                objectMapper.readValue(response, KakaoTokenInfoResponseDto::class.java)

            logger.info { "Access Token 만료기한 : ${tokenInfo.expiresIn}" }
            return tokenInfo
    }

        // 리프레시 토큰으로 액세스 토큰 갱신
        fun refreshAccessToken(refreshToken: String): KakaoRenewTokenResponseDto {
            val params: MultiValueMap<String, String> = LinkedMultiValueMap()
            params.add("grant_type", "refresh_token")
            params.add("client_id", clientId)
            params.add("refresh_token", refreshToken)
            params.add("client_secret", clientSecret)

            val response = WebClient.create("https://kauth.kakao.com")
                .post()
                .uri(tokenUri)
                .body(BodyInserters.fromFormData(params))
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            val objectMapper = ObjectMapper()
            val kakaoToken: KakaoRenewTokenResponseDto =
                objectMapper.readValue(response, KakaoRenewTokenResponseDto::class.java)

            logger.info { "갱신된 Access Token : ${kakaoToken.accessToken}" }
            return kakaoToken
        }
    }