package org.store.clothstar.common.error.exception.order

import org.store.clothstar.common.error.ErrorCode

class InvalidOrderStatusException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)