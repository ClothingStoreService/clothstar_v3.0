package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.exception.OrderErrorCode
import org.store.clothstar.order.exception.OrderNotFoundException
import org.store.clothstar.order.repository.OrderRepository

@Service
class OrderSellerService(
    private val orderRepository: OrderRepository,
) {

    @Transactional
    fun approveOrder(orderId: Long) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(OrderErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusAndDeletedAt(Status.WAITING)
        order.updateStatus(Status.APPROVE)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(OrderErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusAndDeletedAt(Status.WAITING)
        order.updateStatus(Status.CANCEL)
    }
}
