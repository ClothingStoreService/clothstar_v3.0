package org.store.clothstar.member.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedEmailException
import org.store.clothstar.common.error.exception.DuplicatedTelNoException
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.ModifyNameRequest
import org.store.clothstar.member.dto.response.MemberResponse
import org.store.clothstar.member.repository.AccountRepository
import org.store.clothstar.member.repository.MemberRepository

@Service
class MemberServiceImpl(
    private val accountRepository: AccountRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
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

        return memberRepository.findByMemberId(memberId)?.let { member ->
            MemberResponse(
                memberId = member.memberId!!,
                name = member.name,
                telNo = member.telNo,
                totalPaymentPrice = member.memberShoppingActivity.totalPaymentPrice,
                grade = member.memberShoppingActivity.grade
            )
        } ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)
    }

    override fun getMemberByEmail(email: String) {
        accountRepository.findByEmail(email)?.let {
            throw DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL)
        }
    }

    override fun modifyName(memberId: Long, modifyNameRequest: ModifyNameRequest) {
        log.info { "회원 이름 수정 memberId = ${memberId}, name = ${modifyNameRequest.name}" }

        val member = memberRepository.findByMemberId(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        member.updateName(modifyNameRequest)
    }

    override fun updateDeleteAt(memberId: Long) {
        log.info { "회원 삭제 memberId = ${memberId}" }

        val member = memberRepository.findByMemberId(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        member.updateDeletedAt()
    }

    override fun updatePassword(accountId: Long, password: String) {
        log.info { "회원 비밀번호 변경 memberId = ${accountId}" }

        val account = accountRepository.findByAccountId(accountId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_ACCOUNT)

        val encodedPassword = passwordEncoder.encode(password)
        val originalPassword: String = account.password

        //valid check
        if (!passwordEncoder.matches(originalPassword, encodedPassword)) {
            "이전 비밀번호와 같은 비밀번호 입니다."
        }

        account.updatePassword(encodedPassword)
    }

    // TODO member signup, admin signup -> 객체지향 적으로 설계를 어떻게 할 수 있을까
    @Transactional
    override fun saveMember(createMemberDTO: CreateMemberRequest): Long {
        signUpValidCheck(createMemberDTO)

        val member = Member(
            telNo = createMemberDTO.telNo,
            name = createMemberDTO.name,
            memberShoppingActivity = MemberShoppingActivity.init(),
        )

        memberRepository.save(member)

        return member.memberId!!
    }

    override fun getMemberByMemberId(memberId: Long): Member {
        return memberRepository.findByIdOrNull(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)
    }

    fun signUpValidCheck(createMemberDTO: CreateMemberRequest) {
        memberRepository.findByTelNo(createMemberDTO.telNo)?.let {
            throw DuplicatedTelNoException(ErrorCode.DUPLICATED_TEL_NO)
        }
    }
}