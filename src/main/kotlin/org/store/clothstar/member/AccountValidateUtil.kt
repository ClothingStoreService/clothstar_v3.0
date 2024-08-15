package org.store.clothstar.member

import org.springframework.stereotype.Component
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.BadRequestException
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole

@Component
class AccountValidateUtil (

) {

//    fun validateRole(accout: Account, memberRole: MemberRole) {
//        if (!accout.role.equals(memberRole)) {
////            throw BadRequestException(ErrorCode.INVALID_ROLE, accout.accountId, memberRole)
//        }
//    }
}