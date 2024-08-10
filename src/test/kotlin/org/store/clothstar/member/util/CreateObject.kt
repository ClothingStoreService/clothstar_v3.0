package org.store.clothstar.member.util

import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.MemberLoginRequest


class CreateObject {
    companion object {
        private const val email = "test@naver.com"
        private const val password = "test1234"

        fun getCreateMemberRequest(): CreateMemberRequest {
            return CreateMemberRequest(
                email = email,
                password = password,
                name = "현수",
                telNo = "010-1234-1234",
                certifyNum = "adsf",
            )
        }

        fun getCreateMemberRequest(email: String, certifyNum: String): CreateMemberRequest {
            return CreateMemberRequest(
                email = email,
                password = password,
                name = "현수",
                telNo = "010-1234-1234",
                certifyNum = certifyNum,
            )
        }

        fun getMemberLoginRequest(): MemberLoginRequest {
            return MemberLoginRequest(
                email = email,
                password = password,
            )
        }
    }
}