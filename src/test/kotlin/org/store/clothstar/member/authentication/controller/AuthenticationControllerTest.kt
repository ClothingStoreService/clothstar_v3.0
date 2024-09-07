package org.store.clothstar.member.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.InvalidSignupMemberRequest
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.kakaoLogin.service.KakaoLoginService
import org.store.clothstar.member.authentication.domain.SignUpType
import org.store.clothstar.member.dto.request.KakaoMemberRequest
import org.store.clothstar.member.dto.request.SignUpRequest
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val redisUtil: RedisUtil,
    @Autowired private val kakaoLoginService: KakaoLoginService,
) {
    private val MEMBER_URL = "/v1/members"
    private val LOGIN_URL = "/v1/members/login"

    @DisplayName("멤버 일반 회원가입 통합테스트후 로그인 통합테스트")
    @Test
    fun normalSignUpIntegrationTest() {
        //given
        //이메일과 인증번호로 redis 데이터 생성
        val email = "test@naver.com"
        val certifyNum = redisUtil.createdCertifyNum()
        redisUtil.createRedisData(email, certifyNum)

        // 이메일과 인증번호로 create DTO 객체 생성
        val createMemberRequest = CreateObject.getCreateMemberRequest(email, certifyNum)
        val signUpRequest = SignUpRequest(createMemberRequest, null)
        val requestBody = objectMapper.writeValueAsString(signUpRequest)

        // when
        val actions = mockMvc.perform(
            post(MEMBER_URL)
                .param("signUpType", SignUpType.NORMAL.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        //then
        actions.andExpect(status().isCreated)

        //로그인 테스트
        //given
        val memberLoginRequest = CreateObject.getMemberLoginRequest()
        val loginRequestBody = objectMapper.writeValueAsString(memberLoginRequest)

        val loginActions = mockMvc.perform(
            post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestBody)
        )

        loginActions.andExpect(status().isOk)
    }

    @DisplayName("멤버 카카오 회원가입 통합테스트후 로그인 통합테스트")
    @Test
    fun kakaoSignUpIntegrationTest() {
        val mockWebServer = MockWebServer()
        mockWebServer.start()

        //given
        //KakaoLoginService의 tokenUri와 userUri를 MockWebServer로 설정
        kakaoLoginService.tokenUri = mockWebServer.url("/token").toString()
        kakaoLoginService.userUri = mockWebServer.url("/user").toString()

        // 액세스 토큰 반환에 대한 응답 설정
        val tokenResponse = """
        {
            "token_type": "bearer",
            "access_token": "test_access_token",
            "expires_in": 3600,
            "refresh_token": "test_refresh_token",
            "refresh_token_expires_in": 36000,
            "scope": "account_email"
        }
    """

        //사용자 정보 반환에 대한 응답 설정
        val userInfoResponse = """
        {
            "id": 123456789,
            "kakao_account": {
                "email": "test@example.com"
            }
        }
    """

        //토큰 응답 추가(첫 번째 응답)
        mockWebServer.enqueue(
            MockResponse()
                .setBody(tokenResponse)
                .addHeader("Content-Type", "application/json")
        )

        //사용자 정보 응답 추가(두 번째 응답)
        mockWebServer.enqueue(
            MockResponse()
                .setBody(userInfoResponse)
                .addHeader("Content-Type", "application/json")
        )

        //요청 DTO 생성
        val kakaoMemberRequest = KakaoMemberRequest(
            name = "Test User",
            telNo = "010-1234-5678",
            email = null,
            code = "test_code"
        )

        //requestBody 설정
        val signUpRequest = SignUpRequest(null, kakaoMemberRequest)
        val requestBody = objectMapper.writeValueAsString(signUpRequest)

        //when
        val actions = mockMvc.perform(
            post(MEMBER_URL)
                .param("signUpType", SignUpType.KAKAO.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        //then
        actions.andExpect(status().isCreated)

        mockWebServer.shutdown()
    }

    @DisplayName("멤버 카카오 회원가입에서 요청DTO가 NULL일 경우 에러처리 테스트")
    @Test
    fun normalSignUpExceptionTest() {
        //kakaoMemberRequest 요청 DTO 생성
        val kakaoMemberRequest = KakaoMemberRequest(
            name = "Test User",
            telNo = "010-1234-5678",
            email = null,
            code = "test_code"
        )

        // kakaoMemberRequest를 null로 맞춤
        val signUpRequest = SignUpRequest(null, kakaoMemberRequest)
        val requestBody = objectMapper.writeValueAsString(signUpRequest)

        //when & then
        mockMvc.perform(
            post(MEMBER_URL)
                .param("signUpType", SignUpType.NORMAL.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(jsonPath("$.message").value("회원가입 시 회원 정보가 필요합니다."))
    }

    @DisplayName("멤버 카카오 회원가입에서 요청DTO가 NULL일 경우 에러처리 테스트")
    @Test
    fun kakaoSignUpExceptionTest() {
        // createMemberRequest 생성을 위한 이메일과 인증번호로 redis 데이터 생성
        val email = "test@naver.com"
        val certifyNum = redisUtil.createdCertifyNum()
        redisUtil.createRedisData(email, certifyNum)

        // kakaoMemberRequest를 null로 맞춤
        val createMemberRequest = CreateObject.getCreateMemberRequest(email, certifyNum)
        val signUpRequest = SignUpRequest(createMemberRequest, null)
        val requestBody = objectMapper.writeValueAsString(signUpRequest)

        //when & then
            mockMvc.perform(
                post(MEMBER_URL)
                    .param("signUpType", SignUpType.KAKAO.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("회원가입 시 회원 정보가 필요합니다."))
        }
}