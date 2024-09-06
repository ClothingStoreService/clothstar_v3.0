package org.store.clothstar.payment.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.payment.application.PaymentServiceApplication
import org.store.clothstar.payment.dto.request.SavePaymentRequest

@RestController
class PaymentController(
    private val paymentServiceApplication: PaymentServiceApplication,
) {
    private val log = KotlinLogging.logger {}

    @PostMapping("/v1/payments")
    fun savePayment(@RequestBody savePaymentRequest: SavePaymentRequest): ResponseEntity<MessageDTO> {
        log.info { "/v1/payment post 요청 실행" }

        paymentServiceApplication.savePayment(savePaymentRequest)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "상품을 구매 완료 하였습니다."
        )

        return ResponseEntity(messageDTO, HttpStatus.OK)
    }
}