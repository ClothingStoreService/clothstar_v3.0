package org.store.clothstar.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.member.dto.request.CreateAddressRequest

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AddressSaveValidTest(
    @Autowired
    private val mockMvc: MockMvc,

    @Autowired
    private val objectMapper: ObjectMapper,
) {
    private val ADDRESS_ADD_URL: String = "/v1/members/addresses/1"

    @DisplayName("주소 추가시 전화번호 양식을 지켜야 한다.")
    @WithMockUser(username = "현수", roles = ["USER"])
    @Test
    fun addressAdd_telNoValidationTest() {
        //given
        val createAddressRequest = CreateAddressRequest(
            receiverName = "현수",
            zipNo = "12345",
            addressBasic = "서울시 노원구 공릉동",
            addressDetail = "101동",
            telNo = "010",
            deliveryRequest = "문앞",
        )

        val requestBody = objectMapper.writeValueAsString(createAddressRequest)

        //when
        val actions = mockMvc.perform(
            MockMvcRequestBuilders.post(ADDRESS_ADD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        //then
        actions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.telNo").value("유효하지 않은 전화번호 형식입니다."))
    }

    @DisplayName("우편번호는 숫자만 허용된다.")
    @WithMockUser(username = "현수", roles = ["USER"])
    @Test
    fun addressAdd_zipNOValidationTest() {
        //given
        val createAddressRequest = CreateAddressRequest(
            receiverName = "현수",
            zipNo = "우편번호",
            addressBasic = "서울시 노원구 공릉동",
            addressDetail = "101동",
            telNo = "010",
            deliveryRequest = "문앞",
        )

        val requestBody = objectMapper.writeValueAsString(createAddressRequest)

        //when
        val actions = mockMvc.perform(
            MockMvcRequestBuilders.post(ADDRESS_ADD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        //then
        actions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.zipNo").value("우편번호는 숫자만 허용됩니다."))
    }
}