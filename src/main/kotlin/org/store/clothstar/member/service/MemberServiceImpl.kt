package org.store.clothstar.member.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.store.clothstar.common.config.mail.MailContentBuilder
import org.store.clothstar.common.config.mail.MailSendDTO
import org.store.clothstar.common.config.mail.MailService
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedEmailException
import org.store.clothstar.common.error.exception.DuplicatedTelNoException
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.MemberRole
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
    private val mailContentBuilder: MailContentBuilder,
    private val mailService: MailService,
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

    override fun signupCertifyNumEmailSend(email: String) {
        sendEmailAuthentication(email)
        log.info { "인증번호 전송 완료, email = ${email}" }
    }

    override fun getMemberByMemberId(memberId: Long): Member {
        return memberRepository.findByMemberId(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)
    }

    private fun sendEmailAuthentication(toEmail: String): String {
        val certifyNum = redisUtil.createdCertifyNum()
        val message = mailContentBuilder.build(certifyNum)
        val mailSendDTO = MailSendDTO(toEmail, "clothstar 회원가입 인증 메일 입니다.", message)

        mailService.sendMail(mailSendDTO)

        //메일 전송에 성공하면 redis에 key = email, value = 인증번호를 생성한다.
        //지속시간은 10분
        redisUtil.createRedisData(toEmail, certifyNum)

        return certifyNum
    }

    fun verifyEmailCertifyNum(email: String, certifyNum: String): Boolean {
        val certifyNumFoundByRedis = redisUtil.getData(email) ?: return false

        return certifyNumFoundByRedis == certifyNum
    }
}