package org.store.clothstar.order.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.exception.InvalidOrderStatusException
import org.store.clothstar.order.exception.OrderNotFoundException
import org.store.clothstar.order.repository.OrderRepository
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class OrderSellerServiceTest {
    @InjectMockKs
    lateinit var orderSellerService: OrderSellerService

    @MockK
    lateinit var orderRepository: OrderRepository

    @MockK
    lateinit var order: Order

    // 판매자 주문 승인 - approveOrder
    @Test
    @DisplayName("판매자 주문 승인 - 메서드 호출 테스트")
    fun approveOrder_verify_test(){
        //given
        val orderId = 1L
        every { order.status } returns Status.WAITING
        every { orderRepository.findByOrderId(orderId) } returns order
        justRun { order.updateStatus(Status.APPROVE) }

        //when
        orderSellerService.approveOrder(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByOrderId(orderId) }
        verify(exactly = 1) { order.updateStatus(Status.APPROVE) }
    }

    @Test
    @DisplayName("판매자 주문 승인 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun approveOrder_orderNotFound_exception_test(){
        //given
        val orderId = 1L
        every { order.status } returns Status.WAITING
        every { orderRepository.findByOrderId(orderId) } returns null

        //when & then
        assertThrows<OrderNotFoundException> {
            orderSellerService.approveOrder(orderId)
        }
    }

    @Test
    @DisplayName("판매자 주문 승인 - 주문이 '승인대기' 상태가 아닐 때 예외처리 테스트")
    fun approveOrder_invalidOrderStatus_exception_test(){
        //given
        val orderId = 1L
        every { order.status } returns Status.CONFIRM
        every { orderRepository.findByOrderId(orderId) } returns order

        //when & then
        assertThrows<InvalidOrderStatusException> {
            orderSellerService.approveOrder(orderId)
        }
    }

    // 판매자 주문 취소 - cancelOrder
    @Test
    @DisplayName("판매자 주문 취소 - 메서드 호출 테스트")
    fun cancelOrder_verify_test(){
        //given
        val orderId = 1L
        every { order.status } returns Status.WAITING
        every { orderRepository.findByOrderId(orderId) } returns order
        justRun { order.updateStatus(Status.CANCEL) }

        //when
        orderSellerService.cancelOrder(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByOrderId(orderId) }
        verify(exactly = 1) { order.updateStatus(Status.CANCEL) }
    }

    @Test
    @DisplayName("판매자 주문 취소 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun cancelOrder_orderNotFound_exception_test(){
        //given
        val orderId = 1L
        every { order.status } returns Status.WAITING
        every { orderRepository.findByOrderId(orderId) } returns null

        //when & then
        assertThrows<OrderNotFoundException> {
            orderSellerService.cancelOrder(orderId)
        }
    }

    @Test
    @DisplayName("판매자 주문 취소 - 주문이 '승인대기' 상태가 아닐 때 예외처리 테스트")
    fun cancelOrder_invalidOrderStatus_exception_test(){
        //given
        val orderId = 1L
        every { order.status } returns Status.CONFIRM
        every { orderRepository.findByOrderId(orderId) } returns order

        //when & then
        assertThrows<InvalidOrderStatusException> {
            orderSellerService.cancelOrder(orderId)
        }
    }
}