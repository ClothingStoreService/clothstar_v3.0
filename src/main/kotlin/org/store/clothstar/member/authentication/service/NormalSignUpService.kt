package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.store.clothstar.member.application.MemberServiceApplication
import org.store.clothstar.member.dto.request.CreateMemberRequest

@Service("normalSignUpService")
class NormalSignUpService(
    private val memberServiceApplication: MemberServiceApplication
) : SignUpService<CreateMemberRequest> {
    private val log = KotlinLogging.logger {}

    override fun signUp(request: CreateMemberRequest): Long {

        log.info { "NORMALSIGNUPSERVICE 입니다" }

        return memberServiceApplication.signUp(request)
    }
}