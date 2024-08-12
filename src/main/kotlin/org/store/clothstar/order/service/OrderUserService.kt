package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.InsufficientStockException
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.repository.OrderDetailRepository
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.order.service.OrderSave.OrderDetailValidater
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.repository.ProductRepository
import org.store.clothstar.product.service.ItemService
import org.store.clothstar.product.service.ProductService

@Service
class OrderUserService(
    private val orderSaveFacade: OrderSaveFacade,
    private val orderRepository: OrderRepository,
    private val orderDetailRepository: OrderDetailRepository,
    private val productService: ProductService,
    private val itemService: ItemService,
    private val orderDetailValidater: OrderDetailValidater,
) {
    @Transactional
    fun saveOrder(orderRequestWrapper: OrderRequestWrapper): String {
        return orderSaveFacade.saveOrder(orderRequestWrapper)
    }

    // 주문 상세 추가 생성(
    @Transactional
    fun addOrderDetail(addOrderDetailRequest: AddOrderDetailRequest): Long {
        // 요청 DTO와 관련된 order, product, item 불러오기
        val order = orderRepository.findByIdOrNull(addOrderDetailRequest.orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        val product: Product = productService.getProductById(addOrderDetailRequest.productId)
        val item: Item = itemService.getItemById(addOrderDetailRequest.itemId)

        if (addOrderDetailRequest.quantity > item.stock) {
            throw InsufficientStockException(ErrorCode.INSUFFICIENT_STOCK)
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
    fun updateProductStock(item: Item, quantity: Int) {
        val updatedStock = item.stock - quantity
        item.updateStock(updatedStock)
    }

    @Transactional
    fun completeOrder(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusDELIVEREDAndDeletedAt()
        order.updateStatus(Status.COMPLETED)
    }

    @Transactional
    fun cancelOrder(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusCONFIRMEDAndDeletedAt()
        order.updateStatus(Status.CANCELED)
    }

    @Transactional
    fun updateDeleteAt(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForDeletedAt()
        // orderId에 해당하는 주문의 주문상세리스트를 불러온다.
        val orderDetails = orderDetailRepository.findOrderDetailListByOrderId(orderId)
        // 주문상세리스트에 해당하는 각각의 주문상세를 삭제처리한다.
        orderDetails.forEach { it.updateDeletedAt() }
        // 주문을 삭제처리한다.
        order.updateDeletedAt()
    }
}