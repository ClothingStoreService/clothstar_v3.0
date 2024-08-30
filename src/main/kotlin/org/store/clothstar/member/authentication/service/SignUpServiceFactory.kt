package org.store.clothstar.member.authentication.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.store.clothstar.member.authentication.domain.SignUpType
import org.store.clothstar.member.dto.request.CreateMemberRequest

@Component
class SignUpServiceFactory(
    @Qualifier("normalSignUpService") private val normalSignUpService: SignUpService<CreateMemberRequest>,
    @Qualifier("kakaoSignUpService") private val kakaoSignUpService: SignUpService<CreateMemberRequest>
) {
    fun getSignUpService(signUpType: SignUpType): SignUpService<*> {
        return when (signUpType) {
            SignUpType.NORMAL -> normalSignUpService
            SignUpType.KAKAO -> kakaoSignUpService
            else -> throw IllegalArgumentException("지원하지 않는 회원가입 유형입니다.")
        }
    }
}