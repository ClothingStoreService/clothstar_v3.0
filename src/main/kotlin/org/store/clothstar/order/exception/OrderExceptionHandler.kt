package org.store.clothstar.order.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class OrderExceptionHandler {

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFoundException(ex: OrderNotFoundException): ResponseEntity<OrderErrorResponseDTO> {
        val errorResponseDTO = OrderErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }

    @ExceptionHandler(InvalidOrderStatusException::class)
    fun handleInvalidOrderStatusException(ex: InvalidOrderStatusException): ResponseEntity<OrderErrorResponseDTO> {
        val errorResponseDTO = OrderErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }
}