package org.store.clothstar.member.dto.response

import org.store.clothstar.member.domain.MemberGrade

class MemberResponse(
    private var memberId: Long,
    private val name: String,
    private val telNo: String,
    private val totalPaymentPrice: Int,
    private val grade: MemberGrade,
)