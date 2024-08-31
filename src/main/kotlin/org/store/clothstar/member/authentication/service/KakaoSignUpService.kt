package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.store.clothstar.kakaoLogin.service.KakaoLoginService
import org.store.clothstar.member.dto.request.CreateKakaoMemberRequest
import org.store.clothstar.member.dto.request.KakaoMemberRequest
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService

@Service("kakaoSignUpService")
class KakaoSignUpService(
    private val memberService: MemberService,
    private val accountService: AccountService,
    private val kakaoLoginService: KakaoLoginService,
) : SignUpService<KakaoMemberRequest> {
    private val log = KotlinLogging.logger {}

    override fun signUp(request: KakaoMemberRequest): Long {

        val kakaoMemberRequestDTO = CreateKakaoMemberRequest(
            email = request.email!!,
            name = request.name,
            telNo = request.telNo,
        )

        val memberId = memberService.saveKakaoMember(kakaoMemberRequestDTO)
        accountService.saveKakaoAccount(memberId, kakaoMemberRequestDTO)

        log.info { "KAKAOSIGNUPSERVICE 입니다" }

        return memberId
    }
}