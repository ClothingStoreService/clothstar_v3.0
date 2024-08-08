package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.member.service.AddressService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.member.service.SellerService
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.OrderDetailDTO
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.response.OrderResponse
import org.store.clothstar.order.repository.OrderRepository
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.service.ItemService
import org.store.clothstar.product.service.ProductService

@Service
class OrderSellerService(
    private val orderRepository: OrderRepository,
    private val memberService: MemberService,
    private val sellerService: SellerService,
    private val addressService: AddressService,
    private val itemService: ItemService,
    private val productService: ProductService,
) {
    @Transactional(readOnly = true)
    fun getWaitingOrder(): List<OrderResponse> {
        val waitingOrders = orderRepository.findWaitingOrders()

        return waitingOrders
            .map { order ->
                order?.let {
                    val member = memberService.getMemberByMemberId(order.memberId)
                    val seller = sellerService.getSellerById(order.memberId)
                    val address = addressService.getAddressById(order.addressId)
                    val orderResponse = OrderResponse.from(order, member, address)

                    val orderDetails = order.orderDetails
                        .filter { orderDetail -> orderDetail.deletedAt == null }
                        .toList()
                    val itemIds = orderDetails.map(OrderDetail::itemId).toList()
                    val productIds = orderDetails.map(OrderDetail::productId).toList()

                    val items = itemService.findByIdIn(itemIds)
                    val products = productService.findByIdIn(productIds)

                    val itemMap: Map<Long, Item> = items.map { it.itemId!! to it }.toMap()
                    val productMap: Map<Long, Product> = products.map { it.productId!! to it }.toMap()

                    val orderDetailDTOList = orderDetails.map { orderDetail: OrderDetail ->
                        val itemEntity = itemMap[orderDetail.itemId]!!
                        val productEntity = productMap[orderDetail.productId]!!
                        val brandName = seller.brandName
                        OrderDetailDTO.from(orderDetail, itemEntity, productEntity, brandName)
                    }.toList()

                    orderResponse.setterOrderDetailList(orderDetailDTOList)
                    orderResponse
                } ?: throw IllegalArgumentException("오더 리스트가 없습니다.")
            }.toList()
    }

    @Transactional
    fun approveOrder(orderId: Long) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusCONFIRMEDAndDeletedAt()
        order.updateStatus(Status.PROCESSING)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusCONFIRMEDAndDeletedAt()
        order.updateStatus(Status.CANCELED)
    }
}
