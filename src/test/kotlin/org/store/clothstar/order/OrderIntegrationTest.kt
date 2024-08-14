package org.store.clothstar.order

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.category.repository.CategoryJpaRepository
import org.store.clothstar.member.repository.AddressRepository
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.repository.SellerRepository
import org.store.clothstar.member.util.CreateObject
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.utils.CreateOrderObject
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.ProductRepository

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var categoryRepository: CategoryJpaRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var sellerRepository: SellerRepository

    private val ORDER_URL: String = "/v1/orders"

    @Autowired
    private lateinit var orderDetailRepository: OrderDetailRepository

    @DisplayName("단일 주문 조회 통합테스트")
    @Test
    fun testGetOrder() {
        //given
        //
        memberRepository.save(CreateObject.getMember())
        addressRepository.save(CreateObject.getAddress())
        categoryRepository.save(CreateOrderObject.getCategory())
        sellerRepository.save(CreateObject.getSeller())
        productRepository.save(CreateOrderObject.getProduct())
        itemRepository.save(CreateOrderObject.getItem())

        val order: Order = CreateOrderObject.getOrder()
        orderRepository.save(order)
        val orderDetail: OrderDetail = orderDetailRepository.save(CreateOrderObject.getOrderDetail())
        order.addOrderDetail(orderDetail)
        orderRepository.save(order)

        val orderId: String = order.orderId
        val getOrderURL: String = ORDER_URL + "/" + orderId

        //when
        val actions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(getOrderURL)
                .contentType(MediaType.APPLICATION_JSON)
        )

        //then
        actions.andExpect(status().isOk)
            .andExpect(jsonPath("$.orderId").value(orderId))
    }
}