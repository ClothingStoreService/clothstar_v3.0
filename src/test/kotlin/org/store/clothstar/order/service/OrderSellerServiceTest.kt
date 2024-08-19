package org.store.clothstar.order.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
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
import org.store.clothstar.order.domain.vo.*
import org.store.clothstar.order.dto.response.OrderResponse
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.service.ItemService
import org.store.clothstar.product.service.ProductService
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class OrderSellerServiceTest {
    @InjectMockKs
    lateinit var orderSellerService: OrderSellerService

    @MockK
    lateinit var orderRepository: OrderRepository

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
    lateinit var order: Order

    @MockK
    lateinit var member: Member

    @MockK
    lateinit var seller: Seller

    @MockK
    lateinit var address: Address

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

    @MockK
    lateinit var orderDetail: OrderDetail

    val orderId = "4b1a17b5-45f0-455a-a5e3-2c863de18b05"
    val memberId = 1L
    val addressId = 2L
    val productId = 3L
    val itemId = 4L

    // 판매자 주문(CONFIRMED) 리스트 조회
    @Test
    @DisplayName("판매자 주문(CONFIRMED) 리스트 조회 - 성공 테스트")
    fun addOrderDetail_success_test() {
        //given
        every { orderRepository.findConfirmedAndNotDeletedOrders() } returns listOf(order)
        every { order.memberId } returns memberId
        every { order.addressId } returns addressId
        every { memberService.getMemberByMemberId(memberId) } returns member
        every { addressService.getAddressById(addressId) } returns address
        every { sellerService.getSellerById(memberId) } returns seller

        every { totalPrice.shipping } returns 3000
        every { totalPrice.products } returns 5000
        every { totalPrice.payment } returns 8000
        every { order.orderId } returns orderId
        every { member.name } returns "수빈"
        every { order.createdAt } returns LocalDateTime.now()
        every { order.status } returns Status.CONFIRMED
        every { order.paymentMethod } returns PaymentMethod.CARD
        every { order.totalPrice } returns totalPrice
        every { address.addressInfo } returns addressInfo
        every { address.receiverName } returns "수빈"
        every { addressInfo.addressBasic } returns "address1"
        every { addressInfo.addressDetail } returns "address2"
        every { address.telNo } returns "010-1111-1111"
        every { address.deliveryRequest } returns "문앞"

        val expectedorderResponse = OrderResponse.from(order, member, address)
        every { order.orderDetails } returns mutableListOf(orderDetail)
        every { orderDetail.deletedAt } returns null

        // productIds, itemIds로부터 Product/Item 리스트 가져오기
        every { orderDetail.itemId } returns itemId
        every { orderDetail.productId } returns productId
        every { productService.findByProductIdIn(listOf(productId)) } returns listOf(product)
        every { itemService.findByIdIn(listOf(itemId)) } returns listOf(item)

        every { item.itemId } returns itemId
        every { product.productId } returns productId

        every { orderDetail.orderDetailId } returns 1L
        every { product.name } returns "상품이름"
        every { item.name } returns "상품옵션이름"
        every { seller.brandName } returns "brandName"
        every { product.price } returns 1000
        every { orderDetail.quantity } returns 1
        every { orderDetail.price } returns price
        every { item.finalPrice } returns 10000
        every { price.oneKindTotalPrice } returns 10000

        // 주문상세 DTO 리스트 만들기
        val orderDetailDTOs = listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 응답 DTO에 주문상세 DTO 리스트 추가
        expectedorderResponse.updateOrderDetailList(orderDetailDTOs)

        //when
        val orderReponse = orderSellerService.getConfirmedOrders()

        //then
        assertThat(orderReponse).usingRecursiveComparison().isEqualTo(listOf(expectedorderResponse))
        verify(exactly = 1) { orderRepository.findConfirmedAndNotDeletedOrders() }
        verify(exactly = 1) { memberService.getMemberByMemberId(memberId) }
        verify(exactly = 1) { addressService.getAddressById(addressId) }
        verify(exactly = 1) { sellerService.getSellerById(memberId) }
        verify(exactly = 1) { productService.findByProductIdIn(listOf(productId)) }
        verify(exactly = 1) { itemService.findByIdIn(listOf(itemId)) }
    }

    // 판매자 주문 승인 - approveOrder
    @Test
    @DisplayName("판매자 주문 승인 - 성공 테스트")
    fun approveOrder_success_test() {
        //given
        every { order.status } returns Status.CONFIRMED
        every { orderRepository.findByIdOrNull(orderId) } returns order
        justRun { order.validateForStatusCONFIRMEDAndDeletedAt() }
        every { order.updateStatus(Status.PROCESSING) } answers {
            every { order.status } returns Status.PROCESSING
        }

        //when
        orderSellerService.approveOrder(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.PROCESSING) }
        assertEquals(Status.PROCESSING, order.status)
    }

    @Test
    @DisplayName("판매자 주문 승인 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun approveOrder_orderNotFound_exception_test() {
        //given
        every { order.status } returns Status.CONFIRMED
        every { orderRepository.findByIdOrNull(orderId) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderSellerService.approveOrder(orderId)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    // 판매자 주문 취소 - cancelOrder
    @Test
    @DisplayName("판매자 주문 취소 - 성공 테스트")
    fun cancelOrder_success_test() {
        //given
        every { order.status } returns Status.CONFIRMED
        every { orderRepository.findByIdOrNull(orderId) } returns order
        justRun { order.validateForStatusCONFIRMEDAndDeletedAt() }
        every { order.updateStatus(Status.CANCELED) } answers {
            every { order.status } returns Status.CANCELED
        }

        //when
        orderSellerService.cancelOrder(orderId)

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.CANCELED) }
        assertEquals(Status.CANCELED, order.status)
    }

    @Test
    @DisplayName("판매자 주문 취소 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun cancelOrder_orderNotFound_exception_test() {
        //given
        every { order.status } returns Status.CONFIRMED
        every { orderRepository.findByIdOrNull(orderId) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderSellerService.cancelOrder(orderId)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }
}