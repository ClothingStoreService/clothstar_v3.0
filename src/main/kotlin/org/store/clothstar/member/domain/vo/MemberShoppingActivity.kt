package org.store.clothstar.member.domain.vo

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.hibernate.annotations.ColumnDefault
import org.store.clothstar.member.domain.MemberGrade

@Embeddable
class MemberShoppingActivity(
    @ColumnDefault(value = "0")
    val totalPaymentPrice: Long,
    @ColumnDefault(value = "0")
    val point: Int,
    @Enumerated(EnumType.STRING)
    val grade: MemberGrade,
) {
    companion object {
        fun init(): MemberShoppingActivity {
            return MemberShoppingActivity(
                totalPaymentPrice = 0,
                point = 0,
                grade = MemberGrade.BRONZE,
            )
        }
    }
}