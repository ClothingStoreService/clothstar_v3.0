package org.store.clothstar.common.error.exception.order

import org.springframework.http.HttpStatus
import org.store.clothstar.common.error.ErrorCode

class BadRequestException(
    val code: ErrorCode
) : RuntimeException(code.message) {
    val httpStatus: HttpStatus
        get() = code.status

    val errorMessage: String
        get() = code.message
}