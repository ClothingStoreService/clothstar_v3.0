package org.store.clothstar.member.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
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

        //TODO validation check
        memberRepository.findByTelNoOrNull(createMemberDTO.telNo)?.let {
            throw IllegalStateException("이미 존재하는 핸드폰 번호입니다.")
        }

        val member = Member(
            telNo = createMemberDTO.telNo,
            name = createMemberDTO.name,
            memberShoppingActivity = MemberShoppingActivity.init(),
        )

        //TODO CHECK DB configuration <-> JPA Entity
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