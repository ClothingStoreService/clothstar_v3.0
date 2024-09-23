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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.InsufficientStockException
import org.store.clothstar.common.error.exception.order.InvalidOrderStatusException
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
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.dto.request.OrderRequestWrapper
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
    lateinit var orderSaveFacade: OrderSaveFacade

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

    @Test
    @DisplayName("단일 주문 조회 - 성공 테스트")
    fun getOrder_success_test() {
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

        // OrderDetailDTO 생성
        val orderDetailDTOs = listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 기대하는 OrderResponse 생성
        val expectedOrderResponse = OrderResponse.from(order, member, address).apply {
            updateOrderDetailList(orderDetailDTOs)
        }

        // Mock 동작 설정
        every { orderRepository.findByOrderIdAndDeletedAtIsNull(any()) } returns order
        every { memberService.getMemberByMemberId(any()) } returns member
        every { addressService.getAddressById((any())) } returns address
        every { sellerService.getSellerById((any())) } returns seller
        every { productService.findByProductIdIn(any()) } returns listOf(product)
        every { itemService.findByIdIn(any()) } returns listOf(item)

        // when
        val orderResponse = orderUserService.getOrder(order.orderId)

        // then
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(expectedOrderResponse)
        verify(exactly = 1) { orderRepository.findByOrderIdAndDeletedAtIsNull(any()) }
        verify(exactly = 1) { memberService.getMemberByMemberId(any()) }
        verify(exactly = 1) { itemService.findByIdIn(any()) }
        verify(exactly = 1) { productService.findByProductIdIn(any()) }
    }

    @Test
    @DisplayName("단일 주문 조회 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun getOrder_orderNotFound_exception_test() {
        //given
        every { orderRepository.findByOrderIdAndDeletedAtIsNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.getOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    @Test
    @DisplayName("전체 주문 페이징 조회 offset 방식 - 성공 테스트")
    fun getAllOrderOffsetPaging_success_test() {
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

        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val orders: Page<Order> = PageImpl(listOf(order))
        every { orderRepository.findAll(pageable) } returns orders

        // order 관련 member, address, seller 불러오기
        every { memberService.getMemberByMemberId(any()) } returns member
        every { addressService.getAddressById(any()) } returns address
        every { sellerService.getSellerById(any()) } returns seller

        // 응답 DTO 생성(주문상세 리스트는 빈 상태)
        val expectedOrderResponse = OrderResponse.from(order, member, address)

        every { productService.findByProductIdIn(any()) } returns listOf(product)
        every { itemService.findByIdIn(any()) } returns listOf(item)

        // 주문상세 DTO 리스트 만들기
        val orderDetailDTOs: List<OrderDetailDTO> =
            listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 응답 DTO 생성 및 주문상세 DTO 리스트 추가
        expectedOrderResponse.updateOrderDetailList(orderDetailDTOs)

        // when
        val orderResponses: Page<OrderResponse> = orderUserService.getAllOrderOffsetPaging(pageable)

        // then
        assertThat(orderResponses.content).usingRecursiveComparison().isEqualTo(listOf(expectedOrderResponse))
        verify(exactly = 1) { orderRepository.findAll(pageable) }
        verify(exactly = 1) { memberService.getMemberByMemberId(any()) }
        verify(exactly = 1) { addressService.getAddressById(any()) }
        verify(exactly = 1) { sellerService.getSellerById(any()) }
        verify(exactly = 1) { productService.findByProductIdIn(any()) }
        verify(exactly = 1) { itemService.findByIdIn(any()) }
    }

    @Test
    @DisplayName("전체 주문 페이징 조회 slice 방식 - 성공 테스트")
    fun getAllOrderSlicePaging_success_test() {
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

        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val orders: Page<Order> = PageImpl(listOf(order))
        every { orderRepository.findAllByOrderByOrderIdDesc(pageable) } returns orders

        // order 관련 member, address, seller 불러오기
        every { memberService.getMemberByMemberId(any()) } returns member
        every { addressService.getAddressById(any()) } returns address
        every { sellerService.getSellerById(any()) } returns seller

        // 응답 DTO 생성(주문상세 리스트는 빈 상태)
        val expectedOrderResponse = OrderResponse.from(order, member, address)

        // productIds, itemIds로부터 Product/Item 리스트 가져오기
        every { productService.findByProductIdIn(any()) } returns listOf(product)
        every { itemService.findByIdIn(any()) } returns listOf(item)

        // 주문상세 DTO 리스트 만들기
        val orderDetailDTOs: List<OrderDetailDTO> =
            listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 응답 DTO 생성 및 주문상세 DTO 리스트 추가
        expectedOrderResponse.updateOrderDetailList(orderDetailDTOs)

        // when
        val orderResponses: Slice<OrderResponse> = orderUserService.getAllOrderSlicePaging(pageable)

        // then
        assertThat(orderResponses.content).usingRecursiveComparison().isEqualTo(listOf(expectedOrderResponse))
        verify(exactly = 1) { orderRepository.findAllByOrderByOrderIdDesc(pageable) }
        verify(exactly = 1) { memberService.getMemberByMemberId(any()) }
        verify(exactly = 1) { addressService.getAddressById(any()) }
        verify(exactly = 1) { sellerService.getSellerById(any()) }
        verify(exactly = 1) { productService.findByProductIdIn(any()) }
        verify(exactly = 1) { itemService.findByIdIn(any()) }
    }

    @Test
    @DisplayName("주문 저장")
    fun saveOrder() {
        //given
        val orderRequestWrapper = mockk<OrderRequestWrapper>()
        every { orderUserService.saveOrder(orderRequestWrapper) } returns "4b1a17b5-45f0-455a-a5e3-2c863de18b05"

        //when
        orderUserService.saveOrder(orderRequestWrapper)

        //then
        verify(exactly = 1) { orderSaveFacade.saveOrder(orderRequestWrapper) }
    }

    @Test
    @DisplayName("주문 상세 추가 - 성공 테스트")
    fun addOrderDetail_success_test() {
        // Mock 객체 생성 및 설정
        val orderPrice = mockk<Price> {
            every { oneKindTotalPrice } returns 10000
        }

        val item = mockk<Item> {
            every { itemId } returns 1L
            every { stock } returns 10
        }

        val product = mockk<Product> {
            every { productId } returns 1L
            every { price } returns 1000
        }

        val orderTotalPrice = mockk<TotalPrice> {
            every { shipping } returns 3000
            every { products } returns 5000
        }

        val order = mockk<Order> {
            every { orderId } returns "4b1a17b5-45f0-455a-a5e3-2c863de18b05"
            every { status } returns Status.CONFIRMED
            every { totalPrice } returns orderTotalPrice
            every { totalPrice.products } returns 5000
            every { totalPrice.shipping } returns 3000
        }

        val addOrderDetailRequest = mockk<AddOrderDetailRequest> {
            every { orderId } returns order.orderId
            every { productId } returns 1L
            every { itemId } returns 1L
            every { quantity } returns 1
        }

        val orderDetail = mockk<OrderDetail> {
            every { price } returns orderPrice
            every { quantity } returns addOrderDetailRequest.quantity
            every { orderDetailId } returns 1L
        }

        // 요청 DTO와 관련된 order, product, item 불러오기
        every { orderRepository.findByIdOrNull(any()) } returns order
        every { productService.getProductById(any()) } returns product
        every { itemService.getItemById(any()) } returns item

        every { addOrderDetailRequest.toOrderDetail(order, product, item) } returns orderDetail
        every { orderDetailRepository.save(orderDetail) } returns orderDetail

        justRun { order.addOrderDetail(any()) }
        justRun { order.totalPrice.updatePrices(any(), any()) }
        justRun { item.updateStock(any()) }
        justRun { orderUserService.updateProductStock(item, orderDetail.quantity) }

        //when
        val orderDetailId: Long = orderUserService.addOrderDetail(addOrderDetailRequest)

        //then
        assertEquals(orderDetailId, orderDetail.orderDetailId)
        verify(exactly = 1) { orderRepository.findByIdOrNull(any()) }
        verify(exactly = 1) { orderDetailRepository.save(orderDetail) }
    }

    @Test
    @DisplayName("주문 상세 추가 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun addOrderDetail_orderNotFound_exception_test() {
        //given
        val addOrderDetailRequest = AddOrderDetailRequest(
            orderId = "4b1a17b5-45f0-455a-a5e3-2c863de18b05",
            productId = 1L,
            itemId = 1L,
            quantity = 1,
        )
        every { orderRepository.findByIdOrNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.addOrderDetail(addOrderDetailRequest)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    @Test
    @DisplayName("주문 상세 추가 - 주문수량이 재고보다 더 많을 때 예외처리 테스트")
    fun addOrderDetail_insufficientStock_exception_test() {
        //given
        val addOrderDetailRequest = AddOrderDetailRequest(
            orderId = "4b1a17b5-45f0-455a-a5e3-2c863de18b05",
            productId = 1L,
            itemId = 1L,
            quantity = 100,
        )
        val item = mockk<Item> {
            every { stock } returns 10
        }
        val product = mockk<Product> {}
        val order = mockk<Order> {}

        every { orderRepository.findByIdOrNull(any()) } returns order
        every { productService.getProductById(any()) } returns product
        every { itemService.getItemById(any()) } returns item

        //when & then
        val exception = assertThrows<InsufficientStockException> {
            orderUserService.addOrderDetail(addOrderDetailRequest)
        }
        assertEquals(ErrorCode.INSUFFICIENT_STOCK, exception.errorCode)
    }

    @Test
    @DisplayName("주문 상세 추가 - 주문상태가 CONFIRMED가 아닐 때 예외처리 테스트")
    fun addOrderDetail_invalidOrderStatusException_exception_test() {
        //given
        val addOrderDetailRequest = AddOrderDetailRequest(
            orderId = "4b1a17b5-45f0-455a-a5e3-2c863de18b05",
            productId = 1L,
            itemId = 1L,
            quantity = 1,
        )
        val item = mockk<Item> {
            every { stock } returns 10
        }
        val product = mockk<Product> {}
        val order = mockk<Order> {
            every { status } returns Status.DELIVERED
        }

        every { orderRepository.findByIdOrNull(any()) } returns order
        every { productService.getProductById(any()) } returns product
        every { itemService.getItemById(any()) } returns item

        //when & then
        val exception = assertThrows<InvalidOrderStatusException> {
            orderUserService.addOrderDetail(addOrderDetailRequest)
        }
        assertEquals(ErrorCode.INVALID_ORDER_STATUS_CONFIRMED, exception.errorCode)
    }

    // 구매자 구매 확정 - completeOrder
    @Test
    @DisplayName("구매자 구매 확정 - 성공 테스트")
    fun completeOrder_success_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.DELIVERED
        }
        every { orderRepository.findByIdOrNull(any()) } returns order
        justRun { order.validateForStatusDELIVEREDAndDeletedAt() }
        every { order.updateStatus(Status.COMPLETED) } answers {
            every { order.status } returns Status.COMPLETED
        }

        //when
        orderUserService.completeOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(any()) }
        verify(exactly = 1) { order.validateForStatusDELIVEREDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.COMPLETED) }
        assertEquals(Status.COMPLETED, order.status)
    }

    @Test
    @DisplayName("구매자 구매 확정 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun completeOrder_orderNotFound_exception_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.DELIVERED
        }
        every { orderRepository.findByIdOrNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.completeOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    // 구매자 주문 취소 - cancelOrder
    @Test
    @DisplayName("구매자 주문 취소 - 성공 테스트")
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
        orderUserService.cancelOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(any()) }
        verify(exactly = 1) { order.validateForStatusCONFIRMEDAndDeletedAt() }
        verify(exactly = 1) { order.updateStatus(Status.CANCELED) }
        assertEquals(Status.CANCELED, order.status)
    }

    @Test
    @DisplayName("구매자 주문 취소 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun cancelOrder_orderNotFound_exception_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.CONFIRMED
        }
        every { orderRepository.findByIdOrNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.cancelOrder("4b1a17b5-45f0-455a-a5e3-2c863de18b05")
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    // 주문 삭제 - updateDeleteAt
    @Test
    @DisplayName("주문 삭제 - 성공 테스트")
    fun updateDeleteAt_success_test() {
        //given
        val order = mockk<Order> {
            every { status } returns Status.CONFIRMED
        }
        val orderDetail = mockk<OrderDetail> {}

        every { orderRepository.findByIdOrNull(any()) } returns order
        justRun { order.validateForDeletedAt() }
        every { orderDetailRepository.findOrderDetailListByOrderId(any()) } returns listOf(orderDetail)
        every { orderDetail.updateDeletedAt() } answers {
            every { orderDetail.deletedAt } returns LocalDateTime.now()
        }
        every { order.updateDeletedAt() } answers {
            every { order.deletedAt } returns LocalDateTime.now()
        }

        //when
        orderUserService.updateDeleteAt("4b1a17b5-45f0-455a-a5e3-2c863de18b05")

        //then
        verify(exactly = 1) { orderRepository.findByIdOrNull(any()) }
        verify(exactly = 1) { order.validateForDeletedAt() }
        verify(exactly = 1) { orderDetailRepository.findOrderDetailListByOrderId(any()) }
        verify(exactly = 1) { orderDetail.updateDeletedAt() }
        verify(exactly = 1) { order.updateDeletedAt() }

        assertNotNull(order.deletedAt)
        assertNotNull(orderDetail.deletedAt)
    }

    @Test
    @DisplayName("주문 삭제 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun updateDeleteAt_orderNotFound_exception_test() {
        //given
        every { orderRepository.findByIdOrNull(any()) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.updateDeleteAt("4b1a17b5-45f0-455a-a5e3-2c863de18b05")
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }
}