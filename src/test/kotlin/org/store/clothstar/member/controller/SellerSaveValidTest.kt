package org.store.clothstar.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.dto.request.CreateSellerRequest
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.service.SellerService
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SellerSaveValidTest(
    @Autowired
    private var mockMvc: MockMvc,

    @Autowired
    private val objectMapper: ObjectMapper,

    @Autowired
    private val sellerService: SellerService,

    @Autowired
    private val memberRepository: MemberRepository,

    @Autowired
    private val accountRepository: AccountRepository,
) {
    private val SELLER_URL: String = "/v1/sellers"
    private lateinit var member: Member
    private var memberId: Long = 0L

    @DisplayName("회원가입한 멤버아이디와, 인증에 필요한 access 토큰을 가져옵니다.")
    @BeforeEach
    fun getMemberId_getAccessToken() {
        member = memberRepository.save(CreateObject.getMember())
        memberId = member.memberId!!
        accountRepository.save(CreateObject.getAccount(memberId))
    }

    @DisplayName("판매자(Seller) 가입 통합테스트")
    @WithMockUser(roles = ["USER"])
    @Test
    @Throws(Exception::class)
    fun sellerSaveIntegrationTest() {
        //given
        val sellerUrl = SELLER_URL + "/" + memberId
        val createSellerRequest = CreateObject.getCreateSellerRequest(memberId)
        val sellerRequestBody = objectMapper.writeValueAsString(createSellerRequest)

        //when
        val sellerActions = mockMvc.perform(
            MockMvcRequestBuilders.post(sellerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerRequestBody)
        )

        //then
        sellerActions.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
    }

    @DisplayName("판매자 상세 정보 조회 테스트")
    @WithMockUser(roles = ["SELLER"])
    @Test
    @Throws(java.lang.Exception::class)
    fun getSellerTest() {
        //given
        val sellerId: Long = sellerService.sellerSave(memberId, CreateObject.getCreateSellerRequest(memberId))
        val sellerMemberIdURL: String = SELLER_URL + "/" + memberId

        //when
        val actions = mockMvc.perform(
            MockMvcRequestBuilders.get(sellerMemberIdURL)
                .contentType(MediaType.APPLICATION_JSON)
        )

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(sellerId))
    }

    @DisplayName("판매자 가입시 사업자 번호 양식을 체크한다.")
    @WithMockUser(username = "현수", roles = ["SELLER"])
    @Test
    @Throws(java.lang.Exception::class)
    fun sellerSave_bizNoValidationTest() {
        //given
        val createSellerRequest = CreateSellerRequest(
            brandName = "나이키",
            bizNo = "ㅇㅇ"
        )

        val requestBody = objectMapper.writeValueAsString(createSellerRequest)

        //when
        val actions = mockMvc.perform(
            MockMvcRequestBuilders.post(SELLER_URL + "/" + memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )

        //then
        actions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.errorMap.bizNo").value("유효하지 않은 사업자 번호 형식입니다."))
    }
}