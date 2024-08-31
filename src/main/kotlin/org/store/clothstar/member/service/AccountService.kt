package org.store.clothstar.member.service

import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.dto.request.CreateKakaoMemberRequest
import org.store.clothstar.member.dto.request.CreateMemberRequest

interface AccountService {
    fun saveAccount(memberId: Long, createMemberDTO: CreateMemberRequest): Account
    fun saveKakaoAccount(memberId: Long, createKakaoMemberDTO: CreateKakaoMemberRequest): Account
    fun updateRole(memberId: Long, findRole: MemberRole, updateRole: MemberRole)
    fun updateDeletedAt(memberId: Long, findRole: MemberRole)
}