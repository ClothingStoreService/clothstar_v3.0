package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.store.clothstar.kakaoLogin.service.KakaoLoginService
import org.store.clothstar.member.dto.request.CreateKakaoMemberRequest
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.KakaoMemberRequest
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService

@Service("kakaoSignUpService")
class KakaoSignUpService(
    private val memberService: MemberService,
    private val accountService: AccountService,
    private val kakaoLoginService: KakaoLoginService,
): SignUpService<KakaoMemberRequest> {
    private val log = KotlinLogging.logger {}

    override fun signUp(request: KakaoMemberRequest): Long {

//        // 액세스 토큰 받아오기 - 저장 나중에 하기
//        val accessToken = kakaoLoginService.getAccessToken(request.code)
//        // 사용자 정보 받아오기 - 저장 나중에 하기
//        val userInfo = kakaoLoginService.getUserInfo(accessToken.accessToken!!)
//
//        // kakaoMemberRequest의 이메일 필드 업데이트
//        val updatedKakaoMemberRequest = request.addEmail(userInfo.kakaoAccount!!.email!!)

//        val memberKakaoRequestDTO = CreateKakaoMemberRequest(
//            email = request.email!!,
//            name = request.name,
//            telNo = request.telNo,
//        )

        val memberRequestDTO = CreateMemberRequest(
            email = request.email!!,
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