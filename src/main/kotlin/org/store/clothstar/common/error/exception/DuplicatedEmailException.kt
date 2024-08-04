package org.store.clothstar.common.error.exception

import org.store.clothstar.common.error.ErrorCode

class DuplicatedEmailException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
