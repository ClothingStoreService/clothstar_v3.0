package org.store.clothstar.member.application

import org.springframework.stereotype.Service
import org.store.clothstar.dto.request.CreateMemberRequest
import org.store.clothstar.member.service.MemberService

@Service
class MemberServiceApplication(
    private val memberService: MemberService
) {
    fun signUp(createMemberRequest: CreateMemberRequest): Long? {
        return memberService.signUp(createMemberRequest)
    }
}