package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.ProductRepository
import org.store.clothstar.product.service.ItemService

@Service
class OrderDetailService(
    private val orderRepository: OrderRepository,
    private val orderDetailRepository: OrderDetailRepository,
    private val productRepository: ProductRepository,
    private val itemRepository: ItemRepository,
    private val itemService: ItemService,
) {
    // 주문 생성시 같이 호출되는 주문 상세 생성 메서드 - 하나의 트랜잭션으로 묶임
    @Transactional
    fun saveOrderDetailWithOrder(createOrderDetailRequest: CreateOrderDetailRequest, orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다.")

        val product = productRepository.findByIdOrNull(createOrderDetailRequest.productId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다.")

        val item = itemRepository.findByIdOrNull(createOrderDetailRequest.itemId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.")

        // 주문상세 생성 유효성 검사: 주문 수량이 상품 재고보다 클 경우, 주문이 생성되지 않는다.
        if (createOrderDetailRequest.quantity > item.stock) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.")
        }

        val orderDetail = createOrderDetailRequest.toOrderDetail(order, product, item)
        orderDetailRepository.save(orderDetail)

        // 주문 정보 업데이트: 주문 상세 생성에 따른, 총 상품 금액과 총 결제 금액 업데이트
        val newTotalProductsPrice = order.totalPrice.products + orderDetail.price.oneKindTotalPrice
        val newTotalPaymentPrice =
            order.totalPrice.products + order.totalPrice.shipping + orderDetail.price.oneKindTotalPrice

        order.totalPrice.updatePrices(newTotalProductsPrice, newTotalPaymentPrice)

        // 주문 수량만큼 상품 재고 차감
        updateProductStock(item, orderDetail.quantity)
    }

    // 주문 상세 추가 생성
    @Transactional
    fun addOrderDetail(addOrderDetailRequest: AddOrderDetailRequest): Long {
        val order = orderRepository.findByIdOrNull(addOrderDetailRequest.orderId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다.")

        val product = productRepository.findByIdOrNull(addOrderDetailRequest.productId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다.")

        val item = itemRepository.findByIdOrNull(addOrderDetailRequest.itemId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.")

        if (addOrderDetailRequest.quantity > item.stock) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.")
        }

        if (order.status != Status.CONFIRMED) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 입금된 상태에서는 추가 주문이 불가능합니다.")
        }

        val orderDetail = addOrderDetailRequest.toOrderDetail(order, product, item)
        orderDetailRepository.save(orderDetail)

        val newTotalProductsPrice = order.totalPrice.products + orderDetail.price.oneKindTotalPrice
        val newTotalPaymentPrice =
            order.totalPrice.products + order.totalPrice.shipping + orderDetail.price.oneKindTotalPrice

        order.totalPrice.updatePrices(newTotalProductsPrice, newTotalPaymentPrice)

        updateProductStock(item, orderDetail.quantity)

        return orderDetail.orderDetailId!!
    }

    @Transactional
    fun updateDeleteAt(orderDetailId: Long) {
        val orderDetail = orderDetailRepository.findById(orderDetailId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "주문상세 번호를 찾을 수 없습니다.") }

        if (orderDetail.deletedAt != null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 주문입니다.")
        }

        restoreStockByOrderDetail(orderDetailId)
        orderDetail.updateDeletedAt()
    }

    @Transactional
    fun updateProductStock(item: Item, quantity: Int) {
        val updatedStock = item.stock - quantity
        item.updateStock(updatedStock)
    }

    @Transactional
    fun restoreStockByOrderDetail(orderDetailId: Long) {
        val orderDetail = orderDetailRepository.findByIdOrNull(orderDetailId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "주문상세 번호를 찾을 수 없습니다.")

        itemService.restoreProductStockByOrderDetail(orderDetail)
    }
}