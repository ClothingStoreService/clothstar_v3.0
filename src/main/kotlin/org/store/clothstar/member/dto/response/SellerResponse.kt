package org.store.clothstar.member.dto.response

import java.time.LocalDateTime

class SellerResponse(
    private val memberId: Long,
    private val brandName: String,
    private val bizNo: String,
    private val totalSellPrice: Long,
    private val createdAt: LocalDateTime,
)