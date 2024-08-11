package org.store.clothstar.common.config.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.store.clothstar.common.dto.ErrorResponseDTO
import org.store.clothstar.common.dto.ValidErrorResponseDTO
import org.store.clothstar.common.error.exception.*
import org.store.clothstar.common.error.exception.order.InsufficientStockException
import org.store.clothstar.common.error.exception.order.InvalidOrderStatusException
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.common.error.exception.order.OutOfStockException
import java.util.function.Consumer

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = KotlinLogging.logger {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundAccountException::class)
    protected fun memberNotFoundException(ex: NotFoundAccountException): ResponseEntity<ErrorResponseDTO> {
        log.error { "NotFoundAccountException : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            ex.message!!
        )

        return ResponseEntity<ErrorResponseDTO>(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundMemberException::class)
    protected fun memberNotFoundException(ex: NotFoundMemberException): ResponseEntity<ErrorResponseDTO> {
        log.error { "memberNotFoundException : ${ex.message}" }
        ex.fillInStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            ex.message!!
        )

        return ResponseEntity<ErrorResponseDTO>(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedEmailException::class)
    protected fun duplicatedEmailException(ex: DuplicatedEmailException): ResponseEntity<ErrorResponseDTO> {
        log.error { "DuplicatedEmailException : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedTelNoException::class)
    protected fun duplicatedEmailException(ex: DuplicatedTelNoException): ResponseEntity<ErrorResponseDTO> {
        log.error { "DuplicatedTelNoException : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedSellerException::class)
    protected fun duplicatedSellerException(ex: DuplicatedSellerException): ResponseEntity<ErrorResponseDTO> {
        log.error { "DuplicatedSellerException : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedBizNoException::class)
    protected fun duplicatedBizNoException(ex: DuplicatedBizNoException): ResponseEntity<ErrorResponseDTO> {
        log.error { "DuplicatedBizNoException : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedBrandNameException::class)
    protected fun duplicatedBrandNameException(ex: DuplicatedBrandNameException): ResponseEntity<ErrorResponseDTO> {
        log.error { "DuplicatedBrandNameException : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ValidErrorResponseDTO> {
        log.error { "handleMethodArgumentNotValidException : ${ex.message}" }
        ex.printStackTrace()

        val errorMap: MutableMap<String, String> = HashMap()
        ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val message = error.getDefaultMessage()
            errorMap[fieldName] = message!!
        })

        val validErrorResponseDTO = ValidErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            errorMap
        )

        return ResponseEntity<ValidErrorResponseDTO>(validErrorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    private fun mailException(ex: MailException): ResponseEntity<ErrorResponseDTO> {
        log.error { "MailExceptionHandler : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private fun illegalArgumentHandler(ex: IllegalArgumentException): ResponseEntity<ErrorResponseDTO> {
        log.error { "IllegalArgumentHandler : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    private fun exHandler(ex: Exception): ResponseEntity<ErrorResponseDTO> {
        log.error { "ExceptionHandler : ${ex.message}" }
        ex.printStackTrace()

        val errorResponseDTO = ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.message!!
        )

        return ResponseEntity(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NotFoundAddressException::class)
    fun handleAddressNotFoundException(ex: NotFoundAddressException): ResponseEntity<ErrorResponseDTO> {
        val errorResponseDTO = ErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }

    // Order 관련 에러처리
    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFoundException(ex: OrderNotFoundException): ResponseEntity<ErrorResponseDTO> {
        val errorResponseDTO = ErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }

    @ExceptionHandler(InvalidOrderStatusException::class)
    fun handleInvalidOrderStatusException(ex: InvalidOrderStatusException): ResponseEntity<ErrorResponseDTO> {
        val errorResponseDTO = ErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }

    @ExceptionHandler(InsufficientStockException::class)
    fun handleInsufficientStockException(ex: InsufficientStockException): ResponseEntity<ErrorResponseDTO> {
        val errorResponseDTO = ErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }

    @ExceptionHandler(OutOfStockException::class)
    fun handleOutOfStockExceptionException(ex: OutOfStockException): ResponseEntity<ErrorResponseDTO> {
        val errorResponseDTO = ErrorResponseDTO(ex.errorCode.status.value(), ex.errorCode.message)
        return ResponseEntity(errorResponseDTO, ex.errorCode.status)
    }
}