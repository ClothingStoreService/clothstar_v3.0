package org.store.clothstar.payment.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.payment.dto.request.SavePaymentRequest
import org.store.clothstar.payment.service.PaymentService
import org.store.clothstar.product.service.ItemService

@Service
class PaymentServiceApplication(
    private val itemService: ItemService,
    private val paymentService: PaymentService,
) {
    private val log = KotlinLogging.logger {}

    @Transactional
    fun savePayment(savePaymentRequest: SavePaymentRequest) {
        //재고차감
        log.info { "재고차감 로직 실행" }
        val item = itemService.getItemById(savePaymentRequest.itemId)
        itemService.deductItemStock(item, savePaymentRequest.buyQuantity)

        //구매이력 저장
        log.info { "구매이력 저장 실행" }
        paymentService.savePayment(savePaymentRequest)
    }
}