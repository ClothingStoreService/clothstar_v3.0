package org.store.clothstar.order.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.order.domain.vo.PaymentMethod
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.domain.vo.TotalPrice

@Entity(name="orders")
class Order (
    @Id
    val orderId: Long,

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

): BaseEntity() {
fun updateStatus(status: Status) {
        this.status=status;
    }
}