package org.store.clothstar.member.authentication.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val redisUtil: RedisUtil,
) {
    private val MEMBER_URL = "/v1/members"
    private val LOGIN_URL = "/v1/members/login"

    @DisplayName("멤버 회원가입 통합테스트후 로그인 통합테스트")
    @Test
    fun signUpIntegrationTest() {
        //given
        //이메일과 인증번호로 redis 데이터 생성
        val email = "test@naver.com"
        val certifyNum = redisUtil.createdCertifyNum()
        redisUtil.createRedisData(email, certifyNum)

        //이메일과 인증번호로 create DTO 객체 생성
        val createMemberRequest = CreateObject.getCreateMemberRequest(email, certifyNum)
        val requestBody = objectMapper.writeValueAsString(createMemberRequest)

        //when
        val actions = mockMvc.perform(
            post(MEMBER_URL)
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
}