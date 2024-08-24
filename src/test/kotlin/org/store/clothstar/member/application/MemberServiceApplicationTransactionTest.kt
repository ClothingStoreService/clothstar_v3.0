package org.store.clothstar.member.application

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.authentication.service.AuthenticationService
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.member.util.CreateObject


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
@Transactional
class MemberServiceApplicationTransactionTest {
    @MockBean
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var em: EntityManager

    @SpyBean
    lateinit var accountService: AccountService

    @Autowired
    lateinit var memberService: MemberService

    @InjectMockKs
    lateinit var memberServiceApplication: MemberServiceApplication

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    private lateinit var member: Member
    private lateinit var account: Account
    private var memberId: Long = 0L
    private var accountId: Long = 0L

    @BeforeEach
    fun saveMemberAndAccount() {
        member = memberRepository.save(CreateObject.getMember())
        memberId = member.memberId!!
        account = accountRepository.save(CreateObject.getAccount(memberId))
        accountId = account.accountId!!
    }

    @DisplayName("회원을 삭제하면 member, account updatedAt 필드에 값이 update 되는지 확인한다.")
    @Test
    fun deleteTransactionTest() {
        //when
        memberServiceApplication.updateDeleteAt(memberId)
        em.flush()
        em.clear()

        val account = accountRepository.findByIdOrNull(accountId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_ACCOUNT)

        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        //then
        assertThat(account.deletedAt).isNotNull()
        assertThat(member.deletedAt).isNotNull()
    }

    @DisplayName("회원을 삭제하다가 에러나면 전부 롤백되는지 확인한다.")
    @Test
    fun deleteFail_TransactionTest() {
        doThrow(IllegalArgumentException()).`when`(accountService).updateDeletedAt(memberId, MemberRole.USER)
        assertThrows<IllegalArgumentException> { memberServiceApplication.updateDeleteAt(memberId) }
        em.flush()
        em.clear()

        val account = accountRepository.findByIdOrNull(accountId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_ACCOUNT)

        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        //then
        assertThat(account.deletedAt).isNull()
        assertThat(member.deletedAt).isNull()
    }
}