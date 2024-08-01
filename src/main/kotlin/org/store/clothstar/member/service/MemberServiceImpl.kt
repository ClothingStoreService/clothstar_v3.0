package org.store.clothstar.member.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.store.clothstar.common.config.mail.MailContentBuilder
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedEmailException
import org.store.clothstar.common.error.exception.DuplicatedTelNoException
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.response.MemberResponse
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository

@Service
class MemberServiceImpl(
    private val accountRepository: AccountRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mailContentBuilder: MailContentBuilder,
    private val redisUtil: RedisUtil,
) : MemberService {
    private val log = KotlinLogging.logger {}

    override fun getAllMemberOffsetPaging(pageable: Pageable): Page<MemberResponse> {
        return memberRepository.findAllOffsetPaging(pageable)?.map { member ->
            MemberResponse(
                memberId = member.memberId!!,
                name = member.name,
                telNo = member.telNo,
                totalPaymentPrice = member.memberShoppingActivity.totalPaymentPrice,
                grade = member.memberShoppingActivity.grade
            )
        } ?: throw IllegalArgumentException("요청한 페이지 번호의 리스트가 없습니다.")
    }

    override fun getAllMemberSlicePaging(pageable: Pageable): Slice<MemberResponse> {
        return memberRepository.findAllSlicePaging(pageable)?.map { member ->
            MemberResponse(
                memberId = member.memberId!!,
                name = member.name,
                telNo = member.telNo,
                totalPaymentPrice = member.memberShoppingActivity.totalPaymentPrice,
                grade = member.memberShoppingActivity.grade
            )
        } ?: throw IllegalArgumentException("요청한 페이지 번호의 리스트가 없습니다.")
    }

    override fun getMemberById(memberId: Long): MemberResponse {
        log.info { "회원 상세 조회 memberId = ${memberId}" }

//        return memberRepository.findMemberById(memberId)?.let { member ->
//            MemberResponse(
//                memberId = member.memberId!!,
//                name = member.name,
//                telNo = member.telNo,
//                totalPaymentPrice = member.memberShoppingActivity.totalPaymentPrice,
//                grade = member.memberShoppingActivity.grade
//            )
//        }
//            .orElseThrow<NotFoundMemberException> {
//                NotFoundMemberException(
//                    ErrorCode.NOT_FOUND_MEMBER
//                )
//            }
//    }
//
//    override fun getMemberByEmail(email: String?) {
//        if (accountRepository.findByEmail(email!!).isPresent()) {
//            throw DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL)
//        }
//    }

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