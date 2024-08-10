package org.store.clothstar.member.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.ModifyNameRequest
import org.store.clothstar.member.dto.response.MemberResponse

interface MemberService {
    fun getAllMemberOffsetPaging(pageable: Pageable): Page<MemberResponse>

    fun getAllMemberSlicePaging(pageable: Pageable): Slice<MemberResponse>

    fun getMemberById(memberId: Long): MemberResponse

    fun getMemberByEmail(email: String)

    fun updateDeleteAt(memberId: Long)

    fun updatePassword(memberId: Long, password: String)

    fun modifyName(memberId: Long, modifyNameRequest: ModifyNameRequest)

    fun saveMember(createMemberDTO: CreateMemberRequest): Long
    fun signUp(createMemberDTO: CreateMemberRequest): Long

    fun signupCertifyNumEmailSend(email: String)

    fun getMemberByMemberId(memberId: Long): Member
}