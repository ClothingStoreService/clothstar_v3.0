package org.store.clothstar.member.authentication.service

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.store.clothstar.member.application.MemberServiceApplication
import org.store.clothstar.member.dto.request.CreateMemberRequest

@Service("normalSignUpService")
class NormalSignUpService(
    private val memberServiceApplication: MemberServiceApplication
): SignUpService<CreateMemberRequest> {
    override fun signUp(request: CreateMemberRequest): Long {
        return memberServiceApplication.signUp(request)
    }
}