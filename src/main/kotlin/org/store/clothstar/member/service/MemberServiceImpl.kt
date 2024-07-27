package org.store.clothstar.member.service

import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
class MemberServiceImpl(
    private val accountRepository: AccountRepository,
    private val memberRepository: MemberRepository,
) : MemberService {
    override fun signUp(createMemberDTO: CreateMemberRequest): Long? {
        val account = Account(
            email = createMemberDTO.email,
            password = createMemberDTO.password,
            role = MemberRole.USER,
        )
        accountRepository.save(account)

        val member = Member(
            memberId = account.accountId!!,
            telNo = createMemberDTO.telNo,
            name = createMemberDTO.name,
            memberShoppingActivity = MemberShoppingActivity.init(),
        )
        memberRepository.save(member)

        return account.accountId
    }
}