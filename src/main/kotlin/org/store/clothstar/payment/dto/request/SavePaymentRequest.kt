package org.store.clothstar.payment.dto.request

import jakarta.validation.constraints.NotBlank
import org.store.clothstar.payment.domain.Payment

class SavePaymentRequest(
    @field: NotBlank
    var productId: Long,

    @field: NotBlank
    var itemId: Long,

    @field: NotBlank
    var impUid: String,

    @field: NotBlank
    var merchantUid: String,

    @field: NotBlank
    var itemName: String,

    @field: NotBlank
    var itemOption: String,

    @field: NotBlank
    var paidAmount: Int,

    @field: NotBlank
    var buyQuantity: Int,

    @field: NotBlank
    var buyerName: String,

    @field: NotBlank
    var buyerEmail: String,

    @field: NotBlank
    var buyerTelNo: String,

    @field: NotBlank
    var buyerAddr: String,

    @field: NotBlank
    var buyerPostCode: String,
) {
    fun toPayment(): Payment {
        return Payment(
            productId = productId,
            itemId = itemId,
            impUid = impUid,
            merchantUid = merchantUid,
            itemName = itemName,
            itemOption = itemOption,
            paidAmount = paidAmount,
            buyQuantity = buyQuantity,
            buyerName = buyerName,
            buyerEmail = buyerEmail,
            buyerTelNo = buyerTelNo,
            buyerAddr = buyerAddr,
            buyerPostCode = buyerPostCode,
        )
    }
}