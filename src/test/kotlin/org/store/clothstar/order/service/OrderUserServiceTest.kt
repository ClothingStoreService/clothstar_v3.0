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
    val memberId = 1L
    val addressId = 2L
    val productId = 3L
    val itemId = 4L

    // 단일 주문 조회 - getOrder
    @Test
    @DisplayName("단일 주문 조회 - 성공 테스트")
    fun getOrder_success_test() {
        //given
        // orderId 관련 order, member, address, seller 불러오기
        every { orderRepository.findByOrderIdAndDeletedAtIsNull(orderId) } returns order
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

        // 응답 DTO 생성(주문상세 리스트는 빈 상태)
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
        val orderReponse = orderUserService.getOrder(orderId)

        // then
        assertThat(orderReponse).usingRecursiveComparison().isEqualTo(expectedorderResponse)
        verify(exactly = 1) { orderRepository.findByOrderIdAndDeletedAtIsNull(orderId) }
        verify(exactly = 1) { memberService.getMemberByMemberId(memberId) }
        verify(exactly = 1) { itemService.findByIdIn(listOf(itemId)) }
        verify(exactly = 1) { productService.findByProductIdIn(listOf(productId)) }
    }

    @Test
    @DisplayName("단일 주문 조회 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun getOrder_orderNotFound_exception_test() {
        //given
        every { orderRepository.findByOrderIdAndDeletedAtIsNull(orderId) } returns null

        //when & then
        val exception = assertThrows<OrderNotFoundException> {
            orderUserService.getOrder(orderId)
        }
        assertEquals(ErrorCode.NOT_FOUND_ORDER, exception.errorCode)
    }

    @Test
    @DisplayName("전체 주문 페이징 조회 offset 방식 - 성공 테스트")
    fun getAllOrderOffsetPaging_success_test() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val orders: Page<Order> = PageImpl(listOf(order))
        every { orderRepository.findAll(pageable) } returns orders

        // order 관련 member, address, seller 불러오기
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

        // 응답 DTO 생성(주문상세 리스트는 빈 상태)
        val expectedOrderResponse = OrderResponse.from(order, member, address)

        every { order.orderDetails } returns mutableListOf(orderDetail)
        every { orderDetail.deletedAt } returns null

        every { product.productId } returns productId

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
        val orderDetailDTOs: List<OrderDetailDTO> =
            listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 응답 DTO 생성 및 주문상세 DTO 리스트 추가
        expectedOrderResponse.updateOrderDetailList(orderDetailDTOs)

        // when
        val orderResponses: Page<OrderResponse> = orderUserService.getAllOrderOffsetPaging(pageable)

        // then
        assertThat(orderResponses.content).usingRecursiveComparison().isEqualTo(listOf(expectedOrderResponse))
        verify(exactly = 1) { orderRepository.findAll(pageable) }
        verify(exactly = 1) { memberService.getMemberByMemberId(memberId) }
        verify(exactly = 1) { addressService.getAddressById(addressId) }
        verify(exactly = 1) { sellerService.getSellerById(memberId) }
        verify(exactly = 1) { productService.findByProductIdIn(listOf(productId)) }
        verify(exactly = 1) { itemService.findByIdIn(listOf(itemId)) }
    }

    @Test
    @DisplayName("전체 주문 페이징 조회 slice 방식 - 성공 테스트")
    fun getAllOrderSlicePaging_success_test() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val orders: Page<Order> = PageImpl(listOf(order))
        every { orderRepository.findAll(pageable) } returns orders

        // order 관련 member, address, seller 불러오기
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

        // 응답 DTO 생성(주문상세 리스트는 빈 상태)
        val expectedOrderResponse = OrderResponse.from(order, member, address)

        every { order.orderDetails } returns mutableListOf(orderDetail)
        every { orderDetail.deletedAt } returns null

        every { product.productId } returns productId

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
        val orderDetailDTOs: List<OrderDetailDTO> =
            listOf(OrderDetailDTO.from(orderDetail, item, product, seller.brandName))

        // 응답 DTO 생성 및 주문상세 DTO 리스트 추가
        expectedOrderResponse.updateOrderDetailList(orderDetailDTOs)

        // when
        val orderResponses: Slice<OrderResponse> = orderUserService.getAllOrderSlicePaging(pageable)

        // then
        assertThat(orderResponses.content).usingRecursiveComparison().isEqualTo(listOf(expectedOrderResponse))
        verify(exactly = 1) { orderRepository.findAll(pageable) }
        verify(exactly = 1) { memberService.getMemberByMemberId(memberId) }
        verify(exactly = 1) { addressService.getAddressById(addressId) }
        verify(exactly = 1) { sellerService.getSellerById(memberId) }
        verify(exactly = 1) { productService.findByProductIdIn(listOf(productId)) }
        verify(exactly = 1) { itemService.findByIdIn(listOf(itemId)) }
    }

    @Test
    @DisplayName("주문 상세 추가 - 성공 테스트")
    fun addOrderDetail_success_test() {
        // 요청 DTO 모킹
        val addOrderDetailRequest = mockk<AddOrderDetailRequest>()
        every { addOrderDetailRequest.orderId } returns orderId
        every { addOrderDetailRequest.productId } returns productId
        every { addOrderDetailRequest.itemId } returns itemId
        every { addOrderDetailRequest.quantity } returns 1

        // 요청 DTO와 관련된 order, product, item 불러오기
        every { orderRepository.findByIdOrNull(orderId) } returns order
        every { productService.getProductById(productId) } returns product
        every { itemService.getItemById(itemId) } returns item

        every { item.stock } returns 10
        every { order.status } returns Status.CONFIRMED

        every { product.price } returns 1000
        every { product.productId } returns productId
        every { item.itemId } returns itemId

        every { addOrderDetailRequest.toOrderDetail(order, product, item) } returns orderDetail
        every { orderDetailRepository.save(orderDetail) } returns orderDetail

        every { order.totalPrice } returns totalPrice
        every { orderDetail.price } returns price
        every { order.totalPrice.products } returns 10000
        every { orderDetail.price.oneKindTotalPrice } returns 10000
        every { order.totalPrice.shipping } returns 3000

        every { orderDetail.quantity } returns addOrderDetailRequest.quantity

        justRun { order.totalPrice.updatePrices(any(), any()) }
        justRun { item.updateStock(any()) }

        justRun { orderUserService.updateProductStock(item, orderDetail.quantity) }
        every { orderDetail.orderDetailId } returns 123L

        //when
        val orderDetailId: Long = orderUserService.addOrderDetail(addOrderDetailRequest)

        //then
        assertEquals(orderDetailId, orderDetail.orderDetailId)
        verify(exactly = 1) { orderRepository.findByIdOrNull(orderId) }
        verify(exactly = 1) { orderDetailRepository.save(orderDetail) }
    }

    @Test
    @DisplayName("주문 상세 추가 - 주문번호가 존재하지 않을 때 예외처리 테스트")
    fun addOrderDetail_orderNotFound_exception_test() {
        //given
        val addOrderDetailRequest = AddOrderDetailRequest(
            orderId = orderId,
            productId = productId,
            itemId = itemId,
            quantity = 1,
        )
        every { orderRepository.findByIdOrNull(orderId) } returns null

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
            orderId = orderId,
            productId = productId,
            itemId = itemId,
            quantity = 100,
        )
        every { orderRepository.findByIdOrNull(orderId) } returns order
        every { productService.getProductById(productId) } returns product
        every { itemService.getItemById(itemId) } returns item
        every { item.stock } returns 10

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
            orderId = orderId,
            productId = productId,
            itemId = itemId,
            quantity = 1,
        )
        every { orderRepository.findByIdOrNull(orderId) } returns order
        every { productService.getProductById(productId) } returns product
        every { itemService.getItemById(itemId) } returns item
        every { item.stock } returns 10
        every { order.status } returns Status.DELIVERED

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