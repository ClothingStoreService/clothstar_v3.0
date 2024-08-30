package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.store.clothstar.member.application.MemberServiceApplication
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService

@Service("kakaoSignUpService")
class KakaoSignUpService(
    private val memberService: MemberService,
    private val accountService: AccountService
): SignUpService<CreateMemberRequest> {
    private val log = KotlinLogging.logger {}

    override fun signUp(request: CreateMemberRequest): Long {
        val memberId = memberService.saveMember(request)
        accountService.saveAccount(memberId, request)

        log.info { "KAKAOSIGNUPSERVICE 입니다입니다입니다" }

        return memberId
    }
}