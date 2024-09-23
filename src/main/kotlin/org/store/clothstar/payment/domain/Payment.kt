package org.store.clothstar.payment.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val paymentId: Long? = null,

    var productId: Long,
    var itemId: Long,
    var impUid: String,
    var merchantUid: String,
    var itemName: String,
    var itemOption: String,
    var paidAmount: Int,
    var buyQuantity: Int,
    var buyerName: String,
    var buyerEmail: String,
    var buyerTelNo: String,
    var buyerAddr: String,
    var buyerPostCode: String,
) {
}