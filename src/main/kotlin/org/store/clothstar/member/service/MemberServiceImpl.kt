package org.store.clothstar.member.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedTelNoException
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository

@Service
class MemberServiceImpl(
    private val accountRepository: AccountRepository,
    private val memberRepository: MemberRepository,
) : MemberService {

    // TODO member signup, admin signup -> 객체지향 적으로 설계를 어떻게 할 수 있을까
    @Transactional
    override fun signUp(createMemberDTO: CreateMemberRequest): Long {

        memberRepository.findByTelNo(createMemberDTO.telNo)?.let {
            throw DuplicatedTelNoException(ErrorCode.DUPLICATED_TEL_NO)
        }

        val member = Member(
            telNo = createMemberDTO.telNo,
            name = createMemberDTO.name,
            memberShoppingActivity = MemberShoppingActivity.init(),
        )

        memberRepository.save(member)

        val account = member.memberId?.let { memberId ->
            Account(
                email = createMemberDTO.email,
                password = createMemberDTO.password,
                role = MemberRole.USER,
                userId = memberId,
            )
        }

        accountRepository.save(account)

        return member.memberId!!
    }
}