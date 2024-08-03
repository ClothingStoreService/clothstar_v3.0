package org.store.clothstar.order.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.exception.InvalidOrderStatusException
import org.store.clothstar.order.exception.OrderErrorCode
import org.store.clothstar.order.exception.OrderNotFoundException
import org.store.clothstar.order.repository.OrderRepository

@Service
class OrderSellerService(
    private val orderUserRepository: OrderRepository,
) {

    @Transactional
    fun approveOrder(orderId: Long) {
        val order = orderUserRepository.findByOrderId(orderId)
            ?: throw OrderNotFoundException(OrderErrorCode.NOT_FOUND_ORDER)
        if(order.status != Status.WAITING) {
            throw InvalidOrderStatusException(OrderErrorCode.INVALID_ORDER_STATUS)
        }
        order.updateStatus(Status.APPROVE)
    }
}
