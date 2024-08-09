package org.store.clothstar.common.config.exception

class BaseException(
    private val exceptionType: ExceptionType
) : RuntimeException(exceptionType.message())