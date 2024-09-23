package org.store.clothstar.payment.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.payment.dto.request.SavePaymentRequest
import org.store.clothstar.payment.repository.PaymentRepository

@Service
class PaymentService(
    val paymentRepository: PaymentRepository
) {
    @Transactional
    fun savePayment(savePaymentRequest: SavePaymentRequest) {
        val payment = savePaymentRequest.toPayment()
        paymentRepository.save(payment)
    }
}