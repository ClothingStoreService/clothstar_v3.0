package org.store.clothstar.member.dto.response

import org.store.clothstar.member.domain.Seller

class SellerSimpleResponse(
    var memberId: Long,
    val brandName: String,
    val bizNo: String,
) {
    companion object {
        fun getSellerSimpleResponseBySeller(seller: Seller): SellerSimpleResponse {
            return SellerSimpleResponse(
                memberId = seller.memberId,
                bizNo = seller.bizNo,
                brandName = seller.brandName,
            )
        }
    }
}