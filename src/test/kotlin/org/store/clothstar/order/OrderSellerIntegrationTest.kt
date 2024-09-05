package org.store.clothstar.order

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.category.repository.CategoryJpaRepository
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.repository.AddressRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.repository.SellerRepository
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.util.CreateOrderObject
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.ProductRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderSellerIntegrationTest(
    @Autowired
    private val mockMvc: MockMvc,

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
    val ORDER_SELLER_URL = "/v1/orders/seller"

    @DisplayName("판매자 주문(CONFIRMED) 리스트 조회 통합테스트")
    @Test
    fun testGetConfirmedOrder() {
        //given
        createOrdersWithStatus(110, Status.CONFIRMED)

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(ORDER_SELLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
        )

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(10))
    }

    @DisplayName("판매자 주문 승인 통합테스트")
    @Test
    fun testApproveOrder() {
        //given
        val order: Order = createOrderWithStatus(Status.CONFIRMED)
        val approveOrderURL = ORDER_SELLER_URL + "/" + order.orderId + "/process"

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch(approveOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문이 정상적으로 출고처리되었습니다."))

        // 데이터베이스에서 주문 상태 조회하여 검증
        val savedOrder: Order? = orderRepository.findByIdOrNull(order.orderId)
        assertNotNull(savedOrder)
        assertEquals(Status.PROCESSING, savedOrder.status)
    }

    @DisplayName("판매자 주문 취소 통합테스트")
    @Test
    fun testCancelOrder() {
        //given
        val order: Order = createOrderWithStatus(Status.CONFIRMED)
        val cancelOrderURL = ORDER_SELLER_URL + "/" + order.orderId + "/cancel"

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch(cancelOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )

        //then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주문이 정상적으로 취소되었습니다."))

        // 데이터베이스에서 주문 상태 조회하여 검증
        val savedOrder: Order? = orderRepository.findByIdOrNull(order.orderId)
        assertNotNull(savedOrder)
        assertEquals(Status.CANCELED, savedOrder.status)
    }

    fun createOrdersWithStatus(count: Int, status: Status) {
        for (i in 100 until count) {
            val member: Member = memberRepository.save(CreateOrderObject.getMember(i))
            val address = addressRepository.save(CreateOrderObject.getAddress(member))
            val category = categoryRepository.save(CreateOrderObject.getCategory(i))
            sellerRepository.save(CreateOrderObject.getSeller(member, i))
            val product = productRepository.save(CreateOrderObject.getProduct(member, category))
            val item = itemRepository.save(CreateOrderObject.getItem(product))
            val order: Order = CreateOrderObject.getOrder(member, address, i)
            order.updateStatus(status)
            orderRepository.save(order)
            val orderDetail: OrderDetail =
                orderDetailRepository.save(CreateOrderObject.getOrderDetail(product, item, order))
            order.addOrderDetail(orderDetail)
        }
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
}