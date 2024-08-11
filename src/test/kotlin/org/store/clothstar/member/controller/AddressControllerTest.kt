package org.store.clothstar.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.service.AddressService
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AddressControllerTest(
    @Autowired
    private var mockMvc: MockMvc,

    @Autowired
    private val objectMapper: ObjectMapper,

    @Autowired
    private val memberRepository: MemberRepository,

    @Autowired
    private val addressService: AddressService,

    @Autowired
    private val accountRepository: AccountRepository,
) {
    private val ADDRESS_URL: String = "/v1/members/addresses/"
    private lateinit var member: Member
    private var memberId: Long = 0L

    @DisplayName("회원가입한 멤버아이디와, 인증에 필요한 access 토큰을 가져옵니다.")
    @BeforeEach
    fun getMemberId_getAccessToken() {
        member = memberRepository.save(CreateObject.getMember())
        memberId = member.memberId!!
        accountRepository.save(CreateObject.getAccount(memberId))
    }

    @DisplayName("회원 배송지 저장 통합 테스트")
    @WithMockUser
    @Test
    @Throws(Exception::class)
    fun saveMemberAddrTest() {
        //given
        val url: String = ADDRESS_URL + memberId
        val createAddressRequest = CreateObject.getCreateAddressRequest()
        val addressRequestBody = objectMapper.writeValueAsString(createAddressRequest)

        //when
        val actions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressRequestBody)
        )

        //then
        actions.andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("회원 전체 주소 리스트 조회 테스트")
    @WithMockUser
    @Test
    @Throws(java.lang.Exception::class)
    fun memberGetAllAddressTest() {
        //given
        val getMemberAddressURL: String = ADDRESS_URL + memberId
        for (i in 0..4) {
            addressService.addrSave(memberId, CreateObject.getCreateAddressRequest())
        }

        //when
        val actions = mockMvc.perform(
            MockMvcRequestBuilders.get(getMemberAddressURL)
                .contentType(MediaType.APPLICATION_JSON)
        )

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk())
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
    }
}