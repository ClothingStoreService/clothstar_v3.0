package org.store.clothstar.common.error.exception

import org.store.clothstar.common.error.ErrorCode

class DuplicatedTelNoException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
