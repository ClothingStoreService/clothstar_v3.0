package org.store.clothstar.member.controller

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val redisUtil: RedisUtil,
) {
    private val MEMBER_URL = "/v1/members"

    @DisplayName("회원가입 통합테스트")
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
    }

    @DisplayName("회원가입시 이메일 양식을 지켜야 한다.")
    @Test
    @Throws(Exception::class)
    fun signUp_emailValidationTest() {
        //given
        val email = "^^"
        val certifyNum = redisUtil.createdCertifyNum()
        redisUtil.createRedisData(email, certifyNum)
        val createMemberRequest = CreateObject.getCreateMemberRequest(email, certifyNum)
        val requestBody = objectMapper.writeValueAsString(createMemberRequest)

        //when
        val actions = mockMvc.perform(
            post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        //then
        actions.andExpect(status().is4xxClientError())
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.email").value("유효하지 않은 이메일 형식입니다."))
    }
}