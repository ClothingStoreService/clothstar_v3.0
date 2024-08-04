package org.store.clothstar.common.error.exception

import org.store.clothstar.common.error.ErrorCode

class DuplicatedBrandNameException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
