package org.store.clothstar.order

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.category.repository.CategoryJpaRepository
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.repository.AddressRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.repository.SellerRepository
import org.store.clothstar.member.util.OrderComponent
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.PaymentMethod
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.order.dto.request.CreateOrderRequest
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.util.CreateOrderObject
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.ProductRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderUserIntegrationTest(
    @Autowired
    private val mockMvc: MockMvc,

    @Autowired
    private val objectMapper: ObjectMapper,

    @Autowired
    private val orderRepository: OrderRepository,

    @Autowired
    private val memberRepository: MemberRepository,

    @Autowired
    private val addressRepository: AddressRepository,

    @Autowired
    private val categoryRepository: CategoryJpaRepository,

    @Autowired
    private val productRepository: ProductRepository,

    @Autowired
    private val itemRepository: ItemRepository,

    @Autowired
    private val sellerRepository: SellerRepository,

    @Autowired
    private val orderDetailRepository: OrderDetailRepository,
) {
    private val ORDER_URL: String = "/v1/orders"

    @DisplayName("단일 주문 조회 통합테스트")
    @Test
    fun testGetOrder() {
        //given
        val order: Order = createOrder()

        val orderId: String = order.orderId
        val getOrderURL: String = ORDER_URL + "/" + orderId

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(getOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )
        val savedOrder: Order? = orderRepository.findByIdOrNull(order.orderId)

        //then
        actions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.orderId").value(orderId))
        assertEquals(orderId, savedOrder!!.orderId)
        assertEquals(order.memberId, savedOrder.memberId)
        assertEquals(order.addressId, savedOrder.addressId)
        assertEquals(order.status, savedOrder.status)
        assertEquals(order.paymentMethod, savedOrder.paymentMethod)
        assertEquals(order.totalPrice, savedOrder.totalPrice)
    }

    @DisplayName("전체 주문 조회 offset 페이징 통합테스트")
    @Test
    fun testGetAllOrderOffsetPaging() {
        //given
        //필요한 데이터 생성(여러 주문 추가)
        val orders: List<Order> = createOrders(110)

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(ORDER_URL + "/offset")
                .contentType(MediaType.APPLICATION_JSON)
        )
        val expectedOrders: List<Order> = orderRepository.findAll()

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.totalElements").value(10))
            .andExpect(jsonPath("$.totalPages").value(1))
        assertEquals(orders[0].orderId, expectedOrders[0].orderId)
        assertEquals(orders[0].memberId, expectedOrders[0].memberId)
        assertEquals(orders[0].addressId, expectedOrders[0].addressId)
        assertEquals(orders[0].status, expectedOrders[0].status)
        assertEquals(orders[0].paymentMethod, expectedOrders[0].paymentMethod)
        assertEquals(orders[0].totalPrice, expectedOrders[0].totalPrice)
    }

    @DisplayName("전체 주문 조회 slice 페이징 통합테스트")
    @Test
    fun testGetAllOrderSlicePaging() {
        //given
        //필요한 데이터 생성(여러 주문 추가)
        val orders: List<Order> = createOrders(110)

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(ORDER_URL + "/slice")
                .contentType(MediaType.APPLICATION_JSON)
        )
        val expectedOrders: List<Order> = orderRepository.findAll()

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.totalPages").doesNotExist())
            .andExpect(jsonPath("$.totalElements").doesNotExist())
        assertEquals(orders[0].orderId, expectedOrders[0].orderId)
        assertEquals(orders[0].memberId, expectedOrders[0].memberId)
        assertEquals(orders[0].addressId, expectedOrders[0].addressId)
        assertEquals(orders[0].status, expectedOrders[0].status)
        assertEquals(orders[0].paymentMethod, expectedOrders[0].paymentMethod)
        assertEquals(orders[0].totalPrice, expectedOrders[0].totalPrice)
    }

    @Test
    @DisplayName("주문 생성")
    fun testCreateOrder() {
        //given
        val member: Member = memberRepository.save(CreateOrderObject.getMember())
        val address = addressRepository.save(CreateOrderObject.getAddress(member))
        val category = categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateOrderObject.getSeller(member))
        val product = productRepository.save(CreateOrderObject.getProduct(member, category))
        val item = itemRepository.save(CreateOrderObject.getItem(product))

        val createOrderRequest = CreateOrderRequest(
            paymentMethod = PaymentMethod.CARD,
            memberId = member.memberId!!,
            addressId = address.addressId!!
        )
        val createOrderDetailRequest = CreateOrderDetailRequest(
            productId = product.productId!!,
            itemId = item.itemId!!,
            quantity = 1
        )
        val orderRequestWrapper = OrderRequestWrapper(createOrderRequest, createOrderDetailRequest)
        val requestBody = objectMapper.writeValueAsString(orderRequestWrapper)

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        val savedOrders = orderRepository.findAll().filter { it.memberId == member.memberId }
        val savedOrder = savedOrders.firstOrNull()

        //then
        actions.andExpect(jsonPath("$.statusCode").value(200))
            .andExpect(jsonPath("$.message").value("주문이 정상적으로 생성되었습니다."))
        assertEquals(savedOrder!!.memberId, member.memberId)
        assertEquals(1, savedOrder.orderDetails.size)
    }

    @Test
    @DisplayName("주문 생성시 상품재고가 0일경우 에러처리 테스트")
    fun testCreateOrder_itemStockZeroException() {
        //given
        val member: Member = memberRepository.save(CreateOrderObject.getMember())
        val address = addressRepository.save(CreateOrderObject.getAddress(member))
        val category = categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateOrderObject.getSeller(member))
        val product = productRepository.save(CreateOrderObject.getProduct(member, category))
        val item = itemRepository.save(CreateOrderObject.getItem(product)).apply {
            stock = 0
        }

        val createOrderRequest = CreateOrderRequest(
            paymentMethod = PaymentMethod.CARD,
            memberId = member.memberId!!,
            addressId = address.addressId!!
        )
        val createOrderDetailRequest = CreateOrderDetailRequest(
            productId = product.productId!!,
            itemId = item.itemId!!,
            quantity = 1
        )
        val orderRequestWrapper = OrderRequestWrapper(createOrderRequest, createOrderDetailRequest)
        val requestBody = objectMapper.writeValueAsString(orderRequestWrapper)

        //when & then
        mockMvc.perform(
            MockMvcRequestBuilders.post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(jsonPath("$.message").value("품절된 상품입니다."))
    }

    @Test
    @DisplayName("주문 생성시, 주문개수가 상품재고보다 많을 때 재고 부족 예외처리 테스트")
    fun testCreateOrder_InsufficientStock_Exception() {
        //given
        val member: Member = memberRepository.save(CreateOrderObject.getMember())
        val address = addressRepository.save(CreateOrderObject.getAddress(member))
        val category = categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateOrderObject.getSeller(member))
        val product = productRepository.save(CreateOrderObject.getProduct(member, category))
        val item = itemRepository.save(CreateOrderObject.getItem(product)).apply {
            stock = 1
        }

        val createOrderRequest = CreateOrderRequest(
            paymentMethod = PaymentMethod.CARD,
            memberId = member.memberId!!,
            addressId = address.addressId!!
        )
        val createOrderDetailRequest = CreateOrderDetailRequest(
            productId = product.productId!!,
            itemId = item.itemId!!,
            quantity = 5
        )
        val orderRequestWrapper = OrderRequestWrapper(createOrderRequest, createOrderDetailRequest)
        val requestBody = objectMapper.writeValueAsString(orderRequestWrapper)

        //when & then
        mockMvc.perform(
            MockMvcRequestBuilders.post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errorCode").value(400))
            .andExpect(jsonPath("$.message").value("주문 개수가 상품 재고보다 더 많아 요청을 처리할 수 없습니다."))
    }

    @Test
    @DisplayName("주문상세 추가")
    fun testAddOrderDetail() {
        //given
        val orderComponent: OrderComponent = createOrderReturnComponent()
        val addOrderDetailRequest = AddOrderDetailRequest(
            orderId = orderComponent.order.orderId,
            productId = orderComponent.product.productId!!,
            itemId = orderComponent.item.itemId!!,
            quantity = 5
        )
        val requestBody = objectMapper.writeValueAsString(addOrderDetailRequest)

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(ORDER_URL + "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        val savedOrder = orderRepository.findByIdOrNull(orderComponent.order.orderId)

        //then
        actions.andExpect(jsonPath("$.statusCode").value(200))
            .andExpect(jsonPath("$.message").value("주문상세가 정상적으로 생성되었습니다."))
        assertEquals(2, savedOrder!!.orderDetails.size)
        val secondDetail = savedOrder.orderDetails[1]
        assertEquals(secondDetail.quantity, 5)
    }

    @DisplayName("구매 확정 통합테스트")
    @Test
    fun testCompleteOrder() {
        //given
        val order: Order = createOrderWithStatus(Status.DELIVERED)

        val orderId: String = order.orderId
        val completeOrderURL: String = ORDER_URL + "/" + orderId + "/complete"

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch(completeOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )
        val savedOrder: Order? = orderRepository.findByIdOrNull(order.orderId)

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문이 정상적으로 구매 확정 되었습니다."))
        assertEquals(savedOrder!!.status, Status.COMPLETED)
    }

    @DisplayName("주문 취소 통합테스트")
    @Test
    fun testCancelOrder() {
        //given
        val order: Order = createOrderWithStatus(Status.CONFIRMED)

        val orderId: String = order.orderId
        val cancelOrderURL: String = ORDER_URL + "/" + orderId + "/cancel"

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch(cancelOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )
        val savedOrder: Order? = orderRepository.findByIdOrNull(order.orderId)

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문이 정상적으로 취소되었습니다."))
        assertEquals(savedOrder!!.status, Status.CANCELED)
    }

    @DisplayName("주문 삭제 통합테스트")
    @Test
    fun testDeleteOrder() {
        //given
        val order: Order = createOrderWithStatus(Status.CONFIRMED)

        val orderId: String = order.orderId
        val deleteOrderURL: String = ORDER_URL + "/" + orderId

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete(deleteOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )
        val savedOrder: Order? = orderRepository.findByIdOrNull(orderId)
        val deletedTime = LocalDateTime.now()
        val timeDifference = ChronoUnit.SECONDS.between(deletedTime, savedOrder!!.deletedAt)

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문이 정상적으로 삭제되었습니다."))
        assertTrue(timeDifference <= 1)
    }

    fun createOrder(): Order {
        val member: Member = memberRepository.save(CreateOrderObject.getMember())
        val address = addressRepository.save(CreateOrderObject.getAddress(member))
        val category = categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateOrderObject.getSeller(member))
        val product = productRepository.save(CreateOrderObject.getProduct(member, category))
        val item = itemRepository.save(CreateOrderObject.getItem(product))

        val order: Order = orderRepository.save(CreateOrderObject.getOrder(member, address))
        val orderDetail: OrderDetail =
            orderDetailRepository.save(CreateOrderObject.getOrderDetail(product, item, order))
        order.addOrderDetail(orderDetail)

        return order
    }

    fun createOrders(count: Int): List<Order> {
        for (i in 100 until count) {
            val member: Member = memberRepository.save(CreateOrderObject.getMember(i))
            val address = addressRepository.save(CreateOrderObject.getAddress(member))
            val category = categoryRepository.save(CreateOrderObject.getCategory(i))
            sellerRepository.save(CreateOrderObject.getSeller(member, i))
            val product = productRepository.save(CreateOrderObject.getProduct(member, category))
            val item = itemRepository.save(CreateOrderObject.getItem(product))
            val order: Order = orderRepository.save(CreateOrderObject.getOrder(member, address, i))
            val orderDetail: OrderDetail =
                orderDetailRepository.save(CreateOrderObject.getOrderDetail(product, item, order))
            order.addOrderDetail(orderDetail)
        }
        return orderRepository.findAll().toList()
    }

    fun createOrderWithStatus(status: Status): Order {
        val member: Member = memberRepository.save(CreateOrderObject.getMember())
        val address = addressRepository.save(CreateOrderObject.getAddress(member))
        val category = categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateOrderObject.getSeller(member))
        val product = productRepository.save(CreateOrderObject.getProduct(member, category))
        val item = itemRepository.save(CreateOrderObject.getItem(product))

        val order: Order = CreateOrderObject.getOrder(member, address)
        order.updateStatus(status)
        orderRepository.save(order)
        val orderDetail: OrderDetail =
            orderDetailRepository.save(CreateOrderObject.getOrderDetail(product, item, order))
        order.addOrderDetail(orderDetail)

        return order
    }

    fun createOrderReturnComponent(): OrderComponent {
        val member: Member = memberRepository.save(CreateOrderObject.getMember())
        val address = addressRepository.save(CreateOrderObject.getAddress(member))
        val category = categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateOrderObject.getSeller(member))
        val product = productRepository.save(CreateOrderObject.getProduct(member, category))
        val item = itemRepository.save(CreateOrderObject.getItem(product))

        val order: Order = orderRepository.save(CreateOrderObject.getOrder(member, address))
        val orderDetail: OrderDetail =
            orderDetailRepository.save(CreateOrderObject.getOrderDetail(product, item, order))
        order.addOrderDetail(orderDetail)

        return OrderComponent(order, product, item)
    }
}
