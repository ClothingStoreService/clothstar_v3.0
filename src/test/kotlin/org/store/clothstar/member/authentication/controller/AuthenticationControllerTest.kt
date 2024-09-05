//package org.store.clothstar.member.authentication.controller
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.BDDMockito.given
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.http.MediaType
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.springframework.transaction.annotation.Transactional
//import org.store.clothstar.common.config.redis.RedisUtil
//import org.store.clothstar.kakaoLogin.dto.KakaoTokenResponseDto
//import org.store.clothstar.kakaoLogin.dto.KakaoUserInfoResponseDto
//import org.store.clothstar.kakaoLogin.service.KakaoLoginService
//import org.store.clothstar.kakaoLogin.vo.KakaoAccount
//import org.store.clothstar.member.authentication.domain.SignUpType
//import org.store.clothstar.member.dto.request.SignUpRequest
//import org.store.clothstar.member.util.CreateObject
//import kotlin.test.assertEquals
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class AuthenticationControllerTest(
//    @Autowired private val mockMvc: MockMvc,
//    @Autowired private val objectMapper: ObjectMapper,
//    @Autowired private val redisUtil: RedisUtil,
//) {
//    @MockBean
//    private lateinit var kakaoLoginService: KakaoLoginService
//
//    private val MEMBER_URL = "/v1/members"
//    private val LOGIN_URL = "/v1/members/login"
//
//    @DisplayName("멤버 일반 회원가입 통합테스트후 로그인 통합테스트")
//    @Test
//    fun normalSignUpIntegrationTest() {
//        //given
//        //이메일과 인증번호로 redis 데이터 생성
//        val email = "test@naver.com"
//        val certifyNum = redisUtil.createdCertifyNum()
//        redisUtil.createRedisData(email, certifyNum)
//
//        // 이메일과 인증번호로 create DTO 객체 생성
//        val createMemberRequest = CreateObject.getCreateMemberRequest(email, certifyNum)
//        val signUpRequest = SignUpRequest(createMemberRequest, null)
//        val requestBody = objectMapper.writeValueAsString(signUpRequest)
//
//        // when
//        val actions = mockMvc.perform(
//            post(MEMBER_URL)
//                .param("signUpType", SignUpType.NORMAL.toString())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody)
//        )
//
//        //then
//        actions.andExpect(status().isCreated)
//
//        //로그인 테스트
//        //given
//        val memberLoginRequest = CreateObject.getMemberLoginRequest()
//        val loginRequestBody = objectMapper.writeValueAsString(memberLoginRequest)
//
//        val loginActions = mockMvc.perform(
//            post(LOGIN_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(loginRequestBody)
//        )
//
//        loginActions.andExpect(status().isOk)
//    }
//
//    @DisplayName("멤버 카카오 회원가입 통합테스트")
//    @Test
//    fun kakaoSignUpIntegrationTest() {
//        //given
//        val name = "수빈"
//        val telNo = "010-1231-0987"
//        val code = "test_auth_code"
//        val accessToken = "mock_access_token"
//        val refreshToken = "mock_refresh_token"
//        val email = "test@kakao.com"
//
//        // KakaoLoginService가 반환할 Mock 데이터 설정
//        val kakaoTokenResponseDto = KakaoTokenResponseDto("bearer", accessToken, 1000000, refreshToken, 3600)
//        val kakaoUserInfoResponseDto = KakaoUserInfoResponseDto(
//            id = 12345L,
//            kakaoAccount = KakaoAccount(
//                email = email,
//            )
//        )
//
//        // KakaoLoginService의 getAccessToken, getUserInfo 메서드에 대해 mock 설정
//        given(kakaoLoginService.getAccessToken(anyString())).willReturn(kakaoTokenResponseDto)
//        given(kakaoLoginService.getUserInfo(anyString())).willReturn(kakaoUserInfoResponseDto)
//
//        // when - 1: 카카오 콜백 호출
//        val callbackActions = mockMvc.perform(
//            get("/auth/kakao/callback")
//                .param("code", code)
//        )
//
////        assertEquals(code,"test_auth_code")
//
//        // then - 1: 콜백 응답이 예상대로 이루어지는지 확인
//        callbackActions.andExpect(status().isOk)
//
//        // when - 2: 해당 인가 코드로 회원가입 요청
//        val kakaoMemberRequest = CreateObject.getKakaoMemberRequest(name, telNo, code)
//        val signUpRequest = SignUpRequest(null, kakaoMemberRequest)
//        val requestBody = objectMapper.writeValueAsString(signUpRequest)
//
//        //when
//        val actions = mockMvc.perform(
//            post(MEMBER_URL)
//                .param("signUpType", SignUpType.KAKAO.toString())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody)
//        )
//
//        //then
//        actions.andExpect(status().isCreated)
//    }
//}