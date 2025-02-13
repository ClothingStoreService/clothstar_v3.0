package org.store.clothstar.order.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.InvalidOrderStatusException
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.order.domain.vo.PaymentMethod
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.domain.vo.TotalPrice

@Entity(name = "orders")
class Order(
    @Id
    val orderId: String,

    @Column(nullable = false)
    val memberId: Long,

    @Column(nullable = false)
    var addressId: Long,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderDetails: MutableList<OrderDetail> = mutableListOf(),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod,

    @Embedded
    var totalPrice: TotalPrice

) : BaseEntity() {
    fun updateStatus(status: Status) {
        this.status = status
    }

    fun validateForStatusCONFIRMEDAndDeletedAt() {
        if (this.deletedAt != null) {
            throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        }
        if (this.status != Status.CONFIRMED) {
            throw InvalidOrderStatusException(ErrorCode.INVALID_ORDER_STATUS_CONFIRMED)
        }
    }

    fun validateForStatusDELIVEREDAndDeletedAt() {
        if (this.deletedAt != null) {
            throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        }
        if (this.status != Status.DELIVERED) {
            throw InvalidOrderStatusException(ErrorCode.INVALID_ORDER_STATUS_DELIVERED)
        }
    }

    fun validateForDeletedAt() {
        if (this.deletedAt != null) {
            throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        }
    }

    fun addOrderDetail(orderDetail: OrderDetail) {
        orderDetails.add(orderDetail)
        orderDetail.updateOrder(this)
    }
}