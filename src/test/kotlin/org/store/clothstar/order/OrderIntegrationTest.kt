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
import org.store.clothstar.order.util.CreateOrderObject
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.ProductRepository
import org.store.clothstar.member.domain.Member

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderIntegrationTest(
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
    private val ORDER_URL: String = "/v1/orders"

    @DisplayName("단일 주문 조회 통합테스트")
    @Test
    fun testGetOrder() {
            //given
            val member: Member = memberRepository.save(CreateOrderObject.getMember())
            val address = addressRepository.save(CreateOrderObject.getAddress(member))
            val category = categoryRepository.save(CreateOrderObject.getCategory())
            sellerRepository.save(CreateOrderObject.getSeller(member))
            val product = productRepository.save(CreateOrderObject.getProduct(member, category))
            val item = itemRepository.save(CreateOrderObject.getItem(product))

            val order: Order = orderRepository.save(CreateOrderObject.getOrder(member, address))
            val orderDetail: OrderDetail = orderDetailRepository.save(CreateOrderObject.getOrderDetail(product, item, order))
            order.addOrderDetail(orderDetail)

            val orderId: String = order.orderId
            val getOrderURL: String = ORDER_URL + "/" + orderId

            //when
            val actions: ResultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(getOrderURL)
                    .contentType(MediaType.APPLICATION_JSON)
            )

            //then
            actions
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.orderId").value(orderId))
    }
}
