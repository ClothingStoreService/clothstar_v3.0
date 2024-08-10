package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade

@Service
class OrderUserService(
    private val orderSaveFacade: OrderSaveFacade,
    private val orderRepository: OrderRepository,
    private val orderDetailRepository: OrderDetailRepository,
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

    @Transactional
    fun updateDeleteAt(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForDeletedAt()
        // orderId에 해당하는 주문의 주문상세리스트를 불러온다.
        val orderDetails = orderDetailRepository.findOrderDetailListByOrderId(orderId)
        // 주문상세리스트에 해당하는 각각의 주문상세를 삭제처리한다.
        orderDetails.forEach { it.updateDeletedAt() }
        // 주문을 삭제처리한다.
        order.updateDeletedAt()
    }
}