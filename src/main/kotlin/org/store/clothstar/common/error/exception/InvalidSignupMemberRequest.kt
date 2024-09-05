package org.store.clothstar.common.error.exception

import org.store.clothstar.common.error.ErrorCode

class InvalidSignupMemberRequest(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)