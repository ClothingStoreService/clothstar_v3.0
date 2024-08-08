package org.store.clothstar.member.service

import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.dto.request.CreateMemberRequest

interface AccountService {
    fun saveAccount(memberId: Long, createMemberDTO: CreateMemberRequest): Account
}