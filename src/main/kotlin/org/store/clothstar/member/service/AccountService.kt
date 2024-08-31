package org.store.clothstar.member.service

import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.KakaoMemberRequest

interface AccountService {
    fun saveAccount(memberId: Long, createMemberDTO: CreateMemberRequest): Account
    fun saveKakaoAccount(memberId: Long, createKakaoMemberDTO: KakaoMemberRequest): Account
    fun updateRole(memberId: Long, findRole: MemberRole, updateRole: MemberRole)
    fun updateDeletedAt(memberId: Long, findRole: MemberRole)
}