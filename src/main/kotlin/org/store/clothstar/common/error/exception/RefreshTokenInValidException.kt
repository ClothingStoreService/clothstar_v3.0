package org.store.clothstar.common.error.exception

import org.store.clothstar.common.error.ErrorCode

class RefreshTokenInValidException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message)