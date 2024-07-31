package org.store.clothstar.order.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.repository.OrderSellerRepository
import org.store.clothstar.order.repository.OrderUserRepository

@Service
class OrderSellerService(
    private val orderSellerRepository: OrderSellerRepository,
    private val orderUserRepository: OrderUserRepository,
) {

    @Transactional
    fun approveOrder(orderId: Long): MessageDTO {
        val order = orderUserRepository.findByOrderId(orderId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        order.updateStatus(Status.APPROVE)
        orderSellerRepository.save(order)
        return MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.")
    }
}
