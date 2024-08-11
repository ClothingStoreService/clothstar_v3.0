package org.store.clothstar.order.service.OrderSave

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.service.AddressService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.order.dto.request.CreateOrderRequest
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.service.ItemService
import org.store.clothstar.product.service.ProductService

@Service
class OrderSaveFacade(
    private val memberService: MemberService,
    private val addressService: AddressService,
    private val itemService: ItemService,
    private val productService: ProductService,
    private val orderCreator: OrderCreater,
    private val orderDetailValidator: OrderDetailValidater,
    private val orderDetailCreator: OrderDetailCreater,
    private val orderDetailAdder: OrderDetailAdder,
    private val orderSavor: OrderSaver,
    private val orderPriceUpdater: OrderPriceUpdater,
    private val stockUpdator: StockUpdater,
) {
    @Transactional
    fun saveOrder(orderRequestWrapper: OrderRequestWrapper): String {
        // 요청 DTO 불러오기
        val createOrderRequest: CreateOrderRequest = orderRequestWrapper.createOrderRequest
        val createOrderDetailRequest: CreateOrderDetailRequest = orderRequestWrapper.createOrderDetailRequest

        // 요청 DTO와 관련된 member, address, product, item 불러오기
        val member: Member = memberService.getMemberByMemberId(createOrderRequest.memberId)
        val address: Address = addressService.getAddressById(createOrderRequest.addressId)
        val product: Product = productService.getProductById(createOrderDetailRequest.productId)
        val item: Item = itemService.getItemById(createOrderDetailRequest.itemId)

        // 주문상세 생성 유효성 검사
        orderDetailValidator.validateOrder(createOrderDetailRequest, item)

        // 요청 DTO로부터 주문 생성
        val order: Order = orderCreator.createOrder(createOrderRequest, member, address)

        // 주문상세 생성
        val orderDetail: OrderDetail =
            orderDetailCreator.createOrderDetail(createOrderDetailRequest, order, product, item)

        // 주문에 주문상세 추가
        orderDetailAdder.addOrderDetail(order, orderDetail)

        // 주문 정보 업데이트
        orderPriceUpdater.updateOrderPrice(order, orderDetail)

        // 주문 저장 (orderDetail은 cascade 설정에 의해 자동 저장됨)
        orderSavor.saveOrder(order)

        // 주문 수량만큼 상품 재고 차감
        stockUpdator.updateStock(item, orderDetail.quantity)

        return order.orderId
    }
}