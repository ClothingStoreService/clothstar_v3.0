package org.store.clothstar.member.dto.response

import java.time.LocalDateTime

class SellerResponse(
    val memberId: Long,
    val brandName: String,
    val bizNo: String,
    val totalSellPrice: Long,
    val createdAt: LocalDateTime,
)