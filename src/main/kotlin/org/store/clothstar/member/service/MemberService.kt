package org.store.clothstar.member.service

import org.store.clothstar.member.dto.request.CreateMemberRequest

interface MemberService {
    fun signUp(createMemberDTO: CreateMemberRequest): Long?
}