package org.store.clothstar.member.util

import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.CreateAddressRequest
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.dto.request.CreateSellerRequest
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
                telNo = "010-1234-5555",
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

        fun getMember(): Member {
            return Member(
                telNo = "010-1234-4444",
                name = "현수",
                memberShoppingActivity = MemberShoppingActivity.init()
            )
        }

        fun getAccount(userId: Long): Account {
            return Account(
                email = email,
                password = password,
                role = MemberRole.USER,
                userId = userId
            )
        }

        fun getCreateAddressRequest(): CreateAddressRequest {
            return CreateAddressRequest(
                receiverName = "현수",
                zipNo = "12345",
                addressBasic = "공릉동",
                addressDetail = "양지빌라",
                telNo = "010-1234-1234",
                deliveryRequest = "문앞에 놓고 가주세요",
            )
        }

        fun getCreateSellerRequest(memberId: Long): CreateSellerRequest {
            return CreateSellerRequest(
                brandName = "나이키",
                bizNo = "102-12-12345"
            )
        }
    }
}