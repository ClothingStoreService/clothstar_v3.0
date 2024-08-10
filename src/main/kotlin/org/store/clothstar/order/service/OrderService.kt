package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade

@Service
class OrderService(
    private val orderSaveFacade: OrderSaveFacade,
    private val orderRepository: OrderRepository,
) {
    @Transactional
    fun saveOrder(orderRequestWrapper: OrderRequestWrapper): String {
        return orderSaveFacade.saveOrder(orderRequestWrapper)
    }

    @Transactional
    fun completeOrder(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusDELIVEREDAndDeletedAt()
        order.updateStatus(Status.COMPLETED)
    }

    @Transactional
    fun cancelOrder(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusCONFIRMEDAndDeletedAt()
        order.updateStatus(Status.CANCELED)
    }
}