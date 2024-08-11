package org.store.clothstar.member.application

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.member.authentication.service.AuthenticationService
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.member.util.CreateObject

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
@Transactional
class MemberServiceApplicationTest {
    @MockBean
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var memberService: MemberService

    @InjectMockKs
    lateinit var memberServiceApplication: MemberServiceApplication

    @DisplayName("인증번호 확인 생략하고 회원가입을 테스트 한다.")
    @Test
    fun signupTest() {
        val memberId = memberServiceApplication.signUp(CreateObject.getCreateMemberRequest())

        assertThat(memberId).isNotNull()
    }
}