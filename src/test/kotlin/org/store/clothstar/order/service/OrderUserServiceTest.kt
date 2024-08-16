package org.store.clothstar.order.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
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

@ExtendWith(MockKExtension::class)
class OrderUserServiceTest : BehaviorSpec({

    val orderRepository: OrderRepository = mockk()
    val orderDetailRepository: OrderDetailRepository = mockk()
    val memberService: MemberService = mockk()
    val sellerService: SellerService = mockk()
    val addressService: AddressService = mockk()
    val itemService: ItemService = mockk()
    val productService: ProductService = mockk()
    val orderSaveFacade: OrderSaveFacade = mockk()
    val order: Order = mockk()

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
})