package org.store.clothstar.order.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.order.domain.vo.Price

@Entity(name = "order_detail")
class OrderDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderDetailId: Long,

    @Column(name = "product_line_id")
    val productLineId: Long,

    @Column(name = "product_id")
    val productId: Long,

    val quantity: Int,

    @Embedded
    val price: Price,

    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order
) : BaseEntity() {

    fun updateOrder(order: Order) {
        this.order = order
    }
}
