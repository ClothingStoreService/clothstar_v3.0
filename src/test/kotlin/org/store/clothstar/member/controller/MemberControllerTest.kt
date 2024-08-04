package org.store.clothstar.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Disabled
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
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class MemberControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val accountRepository: AccountRepository,
) {
    companion object {
        private val MEMBER_URL = "/v1/members"
    }

//    @Disabled
//    @DisplayName("회원가입 통합테스트")
//    @Test
//    fun signUpIntegrationTest() {
//        //given
//        val createMemberRequest = CreateObject.getCreateMemberRequest()
//        val requestBody = objectMapper.writeValueAsString(createMemberRequest)
//
//        //when
//        val actions = mockMvc.perform(
//            post(MEMBER_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody)
//        )
//
//        //then
//        actions.andExpect(status().isCreated)
//    }
}