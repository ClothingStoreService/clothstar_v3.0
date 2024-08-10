package org.store.clothstar.common.error.exception.order

import org.store.clothstar.common.error.ErrorCode

class OutOfStockException (
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)