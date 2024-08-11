package org.store.clothstar.member.dto.response

import org.store.clothstar.member.domain.MemberGrade

class MemberResponse(
    val memberId: Long,
    val name: String,
    val telNo: String,
    val totalPaymentPrice: Int,
    val grade: MemberGrade,
)