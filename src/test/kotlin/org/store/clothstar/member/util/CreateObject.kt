package org.store.clothstar.member.util

import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.dto.request.CreateMemberRequest


class CreateObject {
    companion object {
        fun getAccount(): Account {
            return Account(
                email = "test@naver.com",
                password = "test1234",
                role = MemberRole.USER,
            )
        }

        fun getCreateMemberRequest(): CreateMemberRequest {
            return CreateMemberRequest(
                email = "test@naver.com",
                password = "test1234",
                name = "현수",
                telNo = "010-1234-1234",
                certifyNum = null,
            )
        }
    }
}