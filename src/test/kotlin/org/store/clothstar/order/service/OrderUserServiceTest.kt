package org.store.clothstar.order.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.member.service.AddressService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.member.service.SellerService
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade
import org.store.clothstar.product.service.ItemService
import org.store.clothstar.product.service.ProductService
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class OrderUserServiceTest: BehaviorSpec({

    val orderRepository: OrderRepository = mockk()
    val orderDetailRepository: OrderDetailRepository = mockk()
    val memberService: MemberService = mockk()
    val sellerService: SellerService = mockk()
    val addressService: AddressService = mockk()
    val itemService: ItemService = mockk()
    val productService: ProductService = mockk()
    val orderSaveFacade: OrderSaveFacade = mockk()
    val order: Order = mockk()
    val orderDetail: OrderDetail = mockk()

    val orderService = OrderUserService(
        orderSaveFacade, orderRepository, orderDetailRepository, memberService, addressService, sellerService,
        productService, itemService
    )

    val orderId = "4b1a17b5-45f0-455a-a5e3-2c863de18b05"

    Given("구매자 구매 확정 - 성공 테스트") {
        every { order.status } returns Status.DELIVERED
        every { orderRepository.findByIdOrNull(orderId) } returns order
        justRun { order.validateForStatusDELIVEREDAndDeletedAt() }
        every { order.updateStatus(Status.COMPLETED) } answers {
            every { order.status } returns Status.COMPLETED
        }
        When("orderId가 존재하면서 정상 요청이 오면") {
            orderService.completeOrder(orderId)
            Then("정상 응답") {
                verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
                verify(exactly = 1) { order.validateForStatusDELIVEREDAndDeletedAt() }
                verify(exactly = 1) { order.updateStatus(Status.COMPLETED) }
                order.status shouldBe Status.COMPLETED
            }
        }
    }

//        When("존재하지 않는 orderId로 잘못된 요청이 오면") {
//            Then("주문이 존재하지 않는 오류 응답")
//        }
//    }

//    @InjectMockKs
//    lateinit var orderService: OrderUserService

//    @MockK
//    lateinit var orderRepository: OrderRepository
//
//    @MockK
//    lateinit var orderDetailRepository: OrderDetailRepository
//
//    @MockK
//    lateinit var memberService: MemberService
//
//    @MockK
//    lateinit var sellerService: SellerService
//
//    @MockK
//    lateinit var addressService: AddressService
//
//    @MockK
//    lateinit var itemService: ItemService
//
//    @MockK
//    lateinit var productService: ProductService
//
//    @MockK
//    lateinit var orderSaveFacade: OrderSaveFacade
//
//    @MockK
//    lateinit var order: Order
//
//    @MockK
//    lateinit var orderDetail: OrderDetail


//    // 구매자 구매 확정 - completeOrder
//    "구매자 구매 확정 - 성공 테스트" {
//        //given
//        every { order.status } returns Status.DELIVERED
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        justRun { order.validateForStatusDELIVEREDAndDeletedAt() }
//        every { order.updateStatus(Status.COMPLETED) } answers {
//            every { order.status } returns Status.COMPLETED
//        }
//
//        //when
//        orderService.completeOrder(orderId)
//
//        //then
//        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
//        verify(exactly = 1) { order.validateForStatusDELIVEREDAndDeletedAt() }
//        verify(exactly = 1) { order.updateStatus(Status.COMPLETED) }
//        assertEquals(Status.COMPLETED, order.status)
//    }
//
//    @Test
//    @DisplayName("구매자 구매 확정 - 주문번호가 존재하지 않을 때 예외처리 테스트")
//    fun completeOrder_orderNotFound_exception_test() {
//        //given
//        every { order.status } returns Status.DELIVERED
//        every { orderRepository.findByIdOrNull(orderId) } returns null
//
//        //when & then
//        val exception = assertThrows<OrderNotFoundException> {
//            orderService.completeOrder(orderId)
//        }
//        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
//    }
//
//    // 구매자 주문 취소 - cancelOrder
//    @Test
//    @DisplayName("구매자 주문 취소 - 성공 테스트")
//    fun cancelOrder_success_test() {
//        //given
//        every { order.status } returns Status.CONFIRMED
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        justRun { order.validateForStatusCONFIRMEDAndDeletedAt() }
//        every { order.updateStatus(Status.CANCELED) } answers {
//            every { order.status } returns Status.CANCELED
//        }
//
//        //when
//        orderService.cancelOrder(orderId)
//
//        //then
//        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
//        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
//        verify(exactly = 1) { order.updateStatus(Status.CANCELED) }
//        order.status shouldBe Status.CANCELED
//    }
//
//    @Test
//    @DisplayName("구매자 주문 취소 - 주문번호가 존재하지 않을 때 예외처리 테스트")
//    fun cancelOrder_orderNotFound_exception_test() {
//        //given
//        every { order.status } returns Status.CONFIRMED
//        every { orderRepository.findByIdOrNull(orderId) } returns null
//
//        //when & then
//        val exception = assertThrows<OrderNotFoundException> {
//            orderService.cancelOrder(orderId)
//        }
//        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
//    }
//
//    // 주문 삭제 - updateDeleteAt
//    @Test
//    @DisplayName("주문 삭제 - 성공 테스트")
//    fun updateDeleteAt_success_test() {
//        //given
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        justRun { order.validateForDeletedAt() }
//        every { orderDetailRepository.findOrderDetailListByOrderId(orderId) } returns listOf(orderDetail)
//        every { orderDetail.updateDeletedAt() } answers {
//            every { orderDetail.deletedAt } returns LocalDateTime.now()
//        }
//        every { order.updateDeletedAt() } answers {
//            every { order.deletedAt } returns LocalDateTime.now()
//        }
//
//        //when
//        orderService.updateDeleteAt(orderId)
//
//        //then
//        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
//        verify(exactly = 1) { order.validateForDeletedAt() }
//        verify(exactly = 1) { orderDetailRepository.findOrderDetailListByOrderId(orderId) }
//        verify(exactly = 1) { orderDetail.updateDeletedAt() }
//        verify(exactly = 1) { order.updateDeletedAt() }
//
//        assertNotNull(order.deletedAt)
//        assertNotNull(orderDetail.deletedAt)
//    }
//
//    @Test
//    @DisplayName("주문 삭제 - 주문번호가 존재하지 않을 때 예외처리 테스트")
//    fun updateDeleteAt_orderNotFound_exception_test() {
//        //given
//        every { orderRepository.findByIdOrNull(orderId) } returns null
//
//        //when & then
//        val exception = assertThrows<OrderNotFoundException> {
//            orderService.updateDeleteAt(orderId)
//        }
//        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
//    }
})