package org.store.clothstar.order.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.repository.OrderRepository

@Service
class OrderSellerService(
    private val orderUserRepository: OrderRepository,
) {

    @Transactional
    fun approveOrder(orderId: Long) {
        val order = orderUserRepository.findByOrderIdAndStatus(orderId,Status.WAITING) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        order.updateStatus(Status.APPROVE)
        orderUserRepository.save(order)
    }
}
