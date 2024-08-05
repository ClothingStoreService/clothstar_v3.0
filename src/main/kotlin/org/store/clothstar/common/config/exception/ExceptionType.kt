package org.store.clothstar.common.config.exception

import org.springframework.http.HttpStatus

interface ExceptionType {
    fun status(): HttpStatus
    fun message(): String
}