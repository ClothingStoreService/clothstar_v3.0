package org.store.clothstar.member.dto.response

import java.time.LocalDateTime

class SellerResponse(
    private var memberId: Long,
    private val brandName: String,
    private val bizNo: String,
    private val totalPaymentPrice: Int,
    private val createdAt: LocalDateTime,
)