package org.store.clothstar.order.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
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

    // 판매자 주문(CONFIRMED) 리스트 조회
    @Test
    @DisplayName("판매자 주문(CONFIRMED) 리스트 조회 - 성공 테스트")
    fun addOrderDetail_success_test() {
        // Mock 객체 생성 및 설정
        val orderPrice = mockk<Price> {
            every { fixedPrice } returns 10000
            every { oneKindTotalPrice } returns 10000
        }

        val orderDetail = mockk<OrderDetail> {
            every { price } returns orderPrice
            every { quantity } returns 1
            every { orderDetailId } returns 1L
            every { deletedAt } returns null
            every { itemId } returns 1L
            every { productId } returns 1L
        }

        val item = mockk<Item> {
            every { itemId } returns 1L
            every { name } returns "상품옵션이름"
            every { finalPrice } returns 10000
        }

        val product = mockk<Product> {
            every { productId } returns 1L
            every { name } returns "상품이름"
            every { price } returns 1000
        }

        val member = mockk<Member> {
            every { name } returns "수빈"
        }

        val orderAddressInfo = mockk<AddressInfo> {
            every { addressBasic } returns "address1"
            every { addressDetail } returns "address2"
        }

        val address = mockk<Address> {
            every { addressInfo } returns orderAddressInfo
            every { receiverName } returns "수빈"
            every { telNo } returns "010-1111-1111"
            every { deliveryRequest } returns "문앞"
        }

        val seller = mockk<Seller> {
            every { brandName } returns "brandName"
        }

        val orderTotalPrice = mockk<TotalPrice> {
            every { shipping } returns 3000
            every { products } returns 5000
            every { payment } returns 8000
        }

        val order = mockk<Order> {
            every { orderId } returns "4b1a17b5-45f0-455a-a5e3-2c863de18b05"
            every { memberId } returns 1L
            every { addressId } returns 1L
            every { createdAt } returns LocalDateTime.now()
            every { status } returns Status.CONFIRMED
            every { paymentMethod } returns PaymentMethod.CARD
            every { totalPrice } returns orderTotalPrice
            every { orderDetails } returns mutableListOf(orderDetail)
        }

        //given
        every { orderRepository.findConfirmedAndNotDeletedOrders() } returns listOf(order)
        every { memberService.getMemberByMemberId(any()) } returns member
        every { addressService.getAddressById(any()) } returns address
        every { sellerService.getSellerById(any()) } returns seller

        val expectedorderResponse = OrderResponse.from(order, member, address)

        // productIds, itemIds로부터 Product/Item 리스트 가져오기
        every { productService.findByProductIdIn(any()) } returns listOf(product)
        every { itemService.findByIdIn(any()) } returns listOf(item)

        // 주문상세 DTO 리스트 만들기
        val orderDetailDTOs = listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 응답 DTO에 주문상세 DTO 리스트 추가
        expectedorderResponse.updateOrderDetailList(orderDetailDTOs)

        //when
        val orderReponse = orderSellerService.getConfirmedOrders()

        //then
        assertThat(orderReponse).usingRecursiveComparison().isEqualTo(listOf(expectedorderResponse))
        verify(exactly = 1) { orderRepository.findConfirmedAndNotDeletedOrders() }
        verify(exactly = 1) { memberService.getMemberByMemberId(any()) }
        verify(exactly = 1) { addressService.getAddressById(any()) }
        verify(exactly = 1) { sellerService.getSellerById(any()) }
        verify(exactly = 1) { productService.findByProductIdIn(any()) }
        verify(exactly = 1) { itemService.findByIdIn(any()) }
    }

    @Test
    @DisplayName("판매자 주문(CONFIRMED) 리스트 조회 - productId가 NULL일 경우 예외처리 테스트")
    fun addOrderDetail_productIdNullException_test() {
        // Mock 객체 생성 및 설정
        val orderPrice = mockk<Price> {
            every { fixedPrice } returns 10000
            every { oneKindTotalPrice } returns 10000
        }

        val orderDetail = mockk<OrderDetail> {
            every { price } returns orderPrice
            every { quantity } returns 1
            every { orderDetailId } returns 1L
            every { deletedAt } returns null
            every { itemId } returns 1L
            every { productId } returns 1L
        }

        val item = mockk<Item> {
            every { itemId } returns 1L
            every { name } returns "상품옵션이름"
            every { finalPrice } returns 10000
        }

        val product = mockk<Product> {
            every { productId } returns 1L
            every { name } returns "상품이름"
            every { price } returns 1000
        }

        val member = mockk<Member> {
            every { name } returns "수빈"
        }

        val orderAddressInfo = mockk<AddressInfo> {
            every { addressBasic } returns "address1"
            every { addressDetail } returns "address2"
        }

        val address = mockk<Address> {
            every { addressInfo } returns orderAddressInfo
            every { receiverName } returns "수빈"
            every { telNo } returns "010-1111-1111"
            every { deliveryRequest } returns "문앞"
        }

        val seller = mockk<Seller> {
            every { brandName } returns "brandName"
        }

        val orderTotalPrice = mockk<TotalPrice> {
            every { shipping } returns 3000
            every { products } returns 5000
            every { payment } returns 8000
        }

        val order = mockk<Order> {
            every { orderId } returns "4b1a17b5-45f0-455a-a5e3-2c863de18b05"
            every { memberId } returns 1L
            every { addressId } returns 1L
            every { createdAt } returns LocalDateTime.now()
            every { status } returns Status.CONFIRMED
            every { paymentMethod } returns PaymentMethod.CARD
            every { totalPrice } returns orderTotalPrice
            every { orderDetails } returns mutableListOf(orderDetail)
        }

        //given
        every { orderRepository.findConfirmedAndNotDeletedOrders() } returns listOf(order)
        every { memberService.getMemberByMemberId(any()) } returns member
        every { addressService.getAddressById(any()) } returns address
        every { sellerService.getSellerById(any()) } returns seller

        //`productService.findByProductIdIn()` 호출 시 빈 리스트 반환 설정
        every { productService.findByProductIdIn(any()) } returns listOf()
        every { itemService.findByIdIn(any()) } returns listOf()

        //when
        val exception = assertThrows<ResponseStatusException> {
            orderSellerService.getConfirmedOrders()
        }

        //then
        assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(exception.reason).isEqualTo("Product not found")
    }

    @Test
    @DisplayName("판매자 주문(CONFIRMED) 리스트 조회 - itemId가 NULL일 경우 예외처리 테스트")
    fun addOrderDetail_itemIdNullException_test() {
        // Mock 객체 생성 및 설정
        val orderPrice = mockk<Price> {
            every { fixedPrice } returns 10000
            every { oneKindTotalPrice } returns 10000
        }

        val orderDetail = mockk<OrderDetail> {
            every { price } returns orderPrice
            every { quantity } returns 1
            every { orderDetailId } returns 1L
            every { deletedAt } returns null
            every { itemId } returns 1L
            every { productId } returns 1L
        }

        val item = mockk<Item> {
            every { itemId } returns 1L
            every { name } returns "상품옵션이름"
            every { finalPrice } returns 10000
        }

        val product = mockk<Product> {
            every { productId } returns 1L
            every { name } returns "상품이름"
            every { price } returns 1000
        }

        val member = mockk<Member> {
            every { name } returns "수빈"
        }

        val orderAddressInfo = mockk<AddressInfo> {
            every { addressBasic } returns "address1"
            every { addressDetail } returns "address2"
        }

        val address = mockk<Address> {
            every { addressInfo } returns orderAddressInfo
            every { receiverName } returns "수빈"
            every { telNo } returns "010-1111-1111"
            every { deliveryRequest } returns "문앞"
        }

        val seller = mockk<Seller> {
            every { brandName } returns "brandName"
        }

        val orderTotalPrice = mockk<TotalPrice> {
            every { shipping } returns 3000
            every { products } returns 5000
            every { payment } returns 8000
        }

        val order = mockk<Order> {
            every { orderId } returns "4b1a17b5-45f0-455a-a5e3-2c863de18b05"
            every { memberId } returns 1L
            every { addressId } returns 1L
            every { createdAt } returns LocalDateTime.now()
            every { status } returns Status.CONFIRMED
            every { paymentMethod } returns PaymentMethod.CARD
            every { totalPrice } returns orderTotalPrice
            every { orderDetails } returns mutableListOf(orderDetail)
        }

        //given
        every { orderRepository.findConfirmedAndNotDeletedOrders() } returns listOf(order)
        every { memberService.getMemberByMemberId(any()) } returns member
        every { addressService.getAddressById(any()) } returns address
        every { sellerService.getSellerById(any()) } returns seller

        //`itemService.findByIdIn()` 호출 시 빈 리스트 반환 설정
        every { productService.findByProductIdIn(any()) } returns listOf(product)
        every { itemService.findByIdIn(any()) } returns listOf()

        //when
        val exception = assertThrows<ResponseStatusException> {
            orderSellerService.getConfirmedOrders()
        }

        //then
        assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(exception.reason).isEqualTo("Item not found")
    }

    // 판매자 주문 승인 - approveOrder
    @Test
    @DisplayName("판매자 주문 승인 - 성공 테스트")
    fun approveOrder_success_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.CONFIRMED
        }
        every { orderRepository.findByIdOrNull(any()) } returns order
        justRun { order.validateForStatusCONFIRMEDAndDeletedAt() }
        every { order.updateStatus(Status.PROCESSING) } answers {
            every { order.status } returns Status.PROCESSING
        }

        //when
        orderSellerService.approveOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(any()) }
        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.PROCESSING) }
        assertEquals(Status.PROCESSING, order.status)
    }

    @Test
    @DisplayName("판매자 주문 승인 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun approveOrder_orderNotFound_exception_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.CONFIRMED
        }
        every { orderRepository.findByIdOrNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderSellerService.approveOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    // 판매자 주문 취소 - cancelOrder
    @Test
    @DisplayName("판매자 주문 취소 - 성공 테스트")
    fun cancelOrder_success_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.CONFIRMED
        }
        every { orderRepository.findByIdOrNull(any()) } returns order
        justRun { order.validateForStatusCONFIRMEDAndDeletedAt() }
        every { order.updateStatus(Status.CANCELED) } answers {
            every { order.status } returns Status.CANCELED
        }

        //when
        orderSellerService.cancelOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(any()) }
        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.CANCELED) }
        assertEquals(Status.CANCELED, order.status)
    }

    @Test
    @DisplayName("판매자 주문 취소 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun cancelOrder_orderNotFound_exception_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.CONFIRMED
        }
        every { orderRepository.findByIdOrNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderSellerService.cancelOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }
}