package org.store.clothstar.member.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.member.authentication.service.AuthenticationService
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.ModifyNameRequest
import org.store.clothstar.member.dto.response.MemberResponse
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.MemberService

@Service
@Transactional
class MemberServiceApplication(
    private val memberService: MemberService,
    private val accountService: AccountService,
    private val authenticationService: AuthenticationService,
) {
    fun getAllMemberOffsetPaging(pageable: Pageable): Page<MemberResponse> {
        return memberService.getAllMemberOffsetPaging(pageable)
    }

    fun getAllMemberSlicePaging(pageable: Pageable): Slice<MemberResponse> {
        return memberService.getAllMemberSlicePaging(pageable)
    }

    fun getMemberById(memberId: Long): MemberResponse {
        return memberService.getMemberById(memberId)
    }

    fun emailCheck(email: String) {
        memberService.getMemberByEmail(email)
    }

    fun modifyName(memberId: Long, modifyNameRequest: ModifyNameRequest) {
        memberService.modifyName(memberId, modifyNameRequest)
    }

    fun modifyPassword(accountId: Long, password: String) {
        memberService.updatePassword(accountId, password)
    }

    @Transactional
    fun updateDeleteAt(memberId: Long) {
        memberService.updateDeleteAt(memberId)
        accountService.updateDeletedAt(memberId, MemberRole.USER)
    }

    @Transactional
    fun signUp(createMemberRequest: CreateMemberRequest): Long {
        //인증번호 확인
        authenticationService.verifyEmailCertifyNum(createMemberRequest.email, createMemberRequest.certifyNum)

        val memberId = memberService.saveMember(createMemberRequest)
        accountService.saveAccount(memberId, createMemberRequest)

        return memberId
    }

    fun signupCertifyNumEmailSend(email: String) {
        authenticationService.signupCertifyNumEmailSend(email)
    }
}