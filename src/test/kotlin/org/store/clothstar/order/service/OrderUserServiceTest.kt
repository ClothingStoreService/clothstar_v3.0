package org.store.clothstar.order.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.Seller
import org.store.clothstar.member.domain.vo.AddressInfo
import org.store.clothstar.member.service.AddressService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.member.service.SellerService
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.OrderDetailDTO
import org.store.clothstar.order.domain.vo.Price
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.domain.vo.TotalPrice
import org.store.clothstar.order.dto.response.OrderResponse
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.service.ItemService
import org.store.clothstar.product.service.ProductService
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class OrderUserServiceTest {
    @InjectMockKs
    lateinit var orderUserService: OrderUserService

    @MockK
    lateinit var orderRepository: OrderRepository

    @MockK
    lateinit var orderDetailRepository: OrderDetailRepository

    @MockK
    lateinit var memberService: MemberService

    @MockK
    lateinit var sellerService: SellerService

    @MockK
    lateinit var addressService: AddressService

    @MockK
    lateinit var itemService: ItemService

    @MockK
    lateinit var productService: ProductService

    @MockK
    lateinit var orderSaveFacade: OrderSaveFacade

    @MockK
    lateinit var order: Order

    @MockK
    lateinit var orderDetail: OrderDetail

    @MockK
    lateinit var member: Member

    @MockK
    lateinit var seller: Seller

    @MockK
    lateinit var address: Address

    @MockK
    lateinit var orderDetails: List<OrderDetail>

    @MockK
    lateinit var product: Product

    @MockK
    lateinit var item: Item

    @MockK
    lateinit var addressInfo: AddressInfo

    @MockK
    lateinit var totalPrice: TotalPrice

    @MockK
    lateinit var price: Price

    val orderId = "4b1a17b5-45f0-455a-a5e3-2c863de18b05"

    // 단일 주문 조회 - getOrder
    @Test
    @DisplayName("단일 주문 조회 - 성공 테스트")
    fun getOrder_success_test() {
        //given
        val memberId = 1L
        val addressId = 2L
        val productId = 3L
        val itemId = 4L

        every { orderRepository.findByOrderIdAndDeletedAtIsNull(orderId) } returns order
        every { order.memberId } returns memberId
        every { order.addressId } returns addressId
        every { order.createdAt } returns LocalDateTime.now()
        every { order.orderDetails } returns mutableListOf(orderDetail)

        every { memberService.getMemberByMemberId(memberId) } returns member
        every { addressService.getAddressById(addressId) } returns address
        every { address.addressInfo } returns addressInfo
        every { order.totalPrice } returns totalPrice
        every { orderDetail.price } returns price
        every { orderDetail.deletedAt } returns null
        every { orderDetail.itemId } returns itemId
        every { orderDetail.productId } returns productId

        every { itemService.findByIdIn(listOf(itemId))} returns listOf(item)
        every { productService.findByProductIdIn(listOf(productId))} returns listOf(product)
        every { item.itemId } returns itemId
        every { product.productId } returns productId
        every { seller.brandName} returns "brandName"

        val orderResponse = OrderResponse.from(order,member,address)


        //when
        val result = orderUserService.getOrder(orderId)

        // then
        verify(exactly = 1) { orderRepository.findByOrderIdAndDeletedAtIsNull(orderId) }
    }

    // 구매자 구매 확정 - completeOrder
    @Test
    @DisplayName("구매자 구매 확정 - 성공 테스트")
    fun completeOrder_success_test() {
        //given
        every { order.status } returns Status.DELIVERED
        every { orderRepository.findByIdOrNull(orderId) } returns order
        justRun { order.validateForStatusDELIVEREDAndDeletedAt() }
        every { order.updateStatus(Status.COMPLETED) } answers {
            every { order.status } returns Status.COMPLETED
        }

        //when
        orderUserService.completeOrder(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
        verify(exactly = 1) { order.validateForStatusDELIVEREDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.COMPLETED) }
        assertEquals(Status.COMPLETED, order.status)
    }

    @Test
    @DisplayName("구매자 구매 확정 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun completeOrder_orderNotFound_exception_test() {
        //given
        every { order.status } returns Status.DELIVERED
        every { orderRepository.findByIdOrNull(orderId) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.completeOrder(orderId)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    // 구매자 주문 취소 - cancelOrder
    @Test
    @DisplayName("구매자 주문 취소 - 성공 테스트")
    fun cancelOrder_success_test() {
        //given
        every { order.status } returns Status.CONFIRMED
        every { orderRepository.findByIdOrNull(orderId) } returns order
        justRun { order.validateForStatusCONFIRMEDAndDeletedAt() }
        every { order.updateStatus(Status.CANCELED) } answers {
            every { order.status } returns Status.CANCELED
        }

        //when
        orderUserService.cancelOrder(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.CANCELED) }
        assertEquals(Status.CANCELED, order.status)
    }

    @Test
    @DisplayName("구매자 주문 취소 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun cancelOrder_orderNotFound_exception_test() {
        //given
        every { order.status } returns Status.CONFIRMED
        every { orderRepository.findByIdOrNull(orderId) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.cancelOrder(orderId)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    // 주문 삭제 - updateDeleteAt
    @Test
    @DisplayName("주문 삭제 - 성공 테스트")
    fun updateDeleteAt_success_test() {
        //given
        every { orderRepository.findByIdOrNull(orderId) } returns order
        justRun { order.validateForDeletedAt() }
        every { orderDetailRepository.findOrderDetailListByOrderId(orderId) } returns listOf(orderDetail)
        every { orderDetail.updateDeletedAt() } answers {
            every { orderDetail.deletedAt } returns LocalDateTime.now()
        }
        every { order.updateDeletedAt() } answers {
            every { order.deletedAt } returns LocalDateTime.now()
        }

        //when
        orderUserService.updateDeleteAt(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
        verify(exactly = 1) { order.validateForDeletedAt() }
        verify(exactly = 1) { orderDetailRepository.findOrderDetailListByOrderId(orderId) }
        verify(exactly = 1) { orderDetail.updateDeletedAt() }
        verify(exactly = 1) { order.updateDeletedAt() }

        assertNotNull(order.deletedAt)
        assertNotNull(orderDetail.deletedAt)
    }

    @Test
    @DisplayName("주문 삭제 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun updateDeleteAt_orderNotFound_exception_test() {
        //given
        every { orderRepository.findByIdOrNull(orderId) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.updateDeleteAt(orderId)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }
}