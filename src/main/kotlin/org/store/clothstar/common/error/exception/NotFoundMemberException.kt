package org.store.clothstar.common.error.exception

import org.store.clothstar.common.error.ErrorCode

class NotFoundMemberException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message)