//package org.store.clothstar.member.authentication.controller
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.assertj.core.api.Assertions
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.MediaType
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.springframework.transaction.annotation.Transactional
//import org.store.clothstar.common.config.redis.RedisUtil
//import org.store.clothstar.member.dto.request.CreateMemberRequest
//import org.store.clothstar.member.dto.request.ModifyPasswordRequest
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//class MemberSignupValidTest(
//    @Autowired private val mockMvc: MockMvc,
//    @Autowired private val objectMapper: ObjectMapper,
//    @Autowired private val redisUtil: RedisUtil,
//) {
//    private val MEMBER_URL = "/v1/members"
//
//    @DisplayName("회원가입시 비밀번호는 최소 8자리 이상이여야 한다.")
//    @Test
//    @Throws(java.lang.Exception::class)
//    fun signUp_passwordValidationTest() {
//        //given
//        val createMemberRequest = CreateMemberRequest(
//            email = "test@test.com",
//            password = "1234567",
//            name = "현수",
//            telNo = "010-1234-1234",
//            certifyNum = "gg"
//        )
//
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
//        actions.andExpect(status().is4xxClientError())
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.password").value("비밀번호는 최소 8자 이상이어야 합니다."))
//    }
//
//    @DisplayName("회원가입시 이름은 필수 값이다.")
//    @Test
//    @Throws(java.lang.Exception::class)
//    fun signUp_nameValidationTest() {
//        //given
//        val createMemberRequest = CreateMemberRequest(
//            email = "test@test.com",
//            password = "1234567",
//            name = "",
//            telNo = "010-1234-1234",
//            certifyNum = "gg"
//        )
//
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
//        Assertions.assertThat(createMemberRequest.password.length).isLessThan(8)
//        actions.andExpect(status().is4xxClientError())
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.name").value("이름은 비어 있을 수 없습니다."))
//    }
//
//    @DisplayName("회원가입시 전화번호 양식이 지켜져야 한다.")
//    @Test
//    @Throws(java.lang.Exception::class)
//    fun signUp_telNoValidationTest() {
//        //given
//        val createMemberRequest = CreateMemberRequest(
//            email = "test@test.com",
//            password = "1234567",
//            name = "",
//            telNo = "010",
//            certifyNum = "gg"
//        )
//
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
//        Assertions.assertThat(createMemberRequest.password.length).isLessThan(8)
//        actions.andExpect(status().is4xxClientError())
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.telNo").value("유효하지 않은 전화번호 형식입니다."))
//    }
//
//    @DisplayName("비밀번호 변경 요청시에도 비밀번호는 8자리 이상이여야 한다.")
//    @WithMockUser(username = "현수", roles = ["USER"])
//    @Test
//    @Throws(java.lang.Exception::class)
//    fun modifyPassword_validCheckTest() {
//        //given
//        val modifyPasswordURL = "/v1/members/pass/1"
//        val modifyPasswordRequest = ModifyPasswordRequest("1")
//        val requestBody = objectMapper.writeValueAsString(modifyPasswordRequest)
//
//        //when
//        val actions = mockMvc.perform(
//            MockMvcRequestBuilders.patch(modifyPasswordURL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody)
//        )
//
//        //then
//        actions.andExpect(status().is4xxClientError())
//        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.password").value("비밀번호는 최소 8자 이상이어야 합니다."))
//    }
//}