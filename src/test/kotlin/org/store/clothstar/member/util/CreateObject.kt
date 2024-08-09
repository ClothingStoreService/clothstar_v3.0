package org.store.clothstar.member.util

import org.store.clothstar.member.dto.request.CreateMemberRequest


class CreateObject {
    companion object {
        fun getCreateMemberRequest(): CreateMemberRequest {
            return CreateMemberRequest(
                email = "test@naver.com",
                password = "test1234",
                name = "현수",
                telNo = "010-1234-1234",
                certifyNum = "adsf",
            )
        }

        fun getCreateMemberRequest(email: String, certifyNum: String): CreateMemberRequest {
            return CreateMemberRequest(
                email = email,
                password = "test1234",
                name = "현수",
                telNo = "010-1234-1234",
                certifyNum = certifyNum,
            )
        }
    }
}