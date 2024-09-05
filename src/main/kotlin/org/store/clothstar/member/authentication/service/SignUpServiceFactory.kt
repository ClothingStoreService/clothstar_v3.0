package org.store.clothstar.member.authentication.service

import org.springframework.stereotype.Component
import org.store.clothstar.member.authentication.domain.SignUpType
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.KakaoMemberRequest

@Component
class SignUpServiceFactory(
    private val normalSignUpService: SignUpService<CreateMemberRequest>,
    private val kakaoSignUpService: SignUpService<KakaoMemberRequest>
) {
    fun getSignUpService(signUpType: SignUpType): SignUpService<*> {
        return when (signUpType) {
            SignUpType.NORMAL -> normalSignUpService
            SignUpType.KAKAO -> kakaoSignUpService
        }
    }
}