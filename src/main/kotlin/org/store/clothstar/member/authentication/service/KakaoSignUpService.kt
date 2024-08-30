package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.KakaoMemberRequest
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService

@Service("kakaoSignUpService")
class KakaoSignUpService(
    private val memberService: MemberService,
    private val accountService: AccountService
): SignUpService<KakaoMemberRequest> {
    private val log = KotlinLogging.logger {}

    override fun signUp(request: KakaoMemberRequest): Long {

        val memberRequestDTO = CreateMemberRequest(
            email = "asdasd@gmail.com",
            password = "123123123",
            name = request.name,
            telNo = request.telNo,
            certifyNum = "1111",
        )

        val memberId = memberService.saveMember(memberRequestDTO)
        accountService.saveAccount(memberId, memberRequestDTO)

        log.info { "KAKAOSIGNUPSERVICE 입니다입니다입니다" }

        return memberId
    }
}