package org.store.clothstar.member.service

import org.store.clothstar.dto.request.CreateMemberRequest

interface MemberService {
    fun signUp(createMemberDTO: CreateMemberRequest): Long?
}