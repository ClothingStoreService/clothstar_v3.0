package org.store.clothstar.common.error.exception.order

import org.store.clothstar.common.error.ErrorCode

class OrderNotFoundException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)