package org.store.clothstar.member.util

import org.store.clothstar.member.domain.*
import org.store.clothstar.member.domain.vo.AddressInfo
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.dto.request.*

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

        fun getKakaoMemberRequest(name: String, telNo: String, code: String): KakaoMemberRequest {
            return KakaoMemberRequest(
                name = name,
                telNo = telNo,
                code = code,
                email = null,
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
                memberId = 1L,
                telNo = "010-1234-4444",
                name = "현수",
                memberShoppingActivity = MemberShoppingActivity.init()
            )
        }

        fun getAddress(): Address {
            return Address(
                addressId = 1L,
                receiverName = "현수",
                telNo = "010-1234-4444",
                memberId = this.getMember().memberId!!,
                deliveryRequest = "문 앞에 놔주세요",
                addressInfo = AddressInfo.init()
            )
        }

        fun getSeller(): Seller {
            return Seller(
                memberId = this.getMember().memberId!!,
                brandName = "나이키",
                bizNo = "123-123",
                totalSellPrice = 1000
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