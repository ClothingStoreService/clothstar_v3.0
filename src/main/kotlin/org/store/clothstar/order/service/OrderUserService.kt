package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.InsufficientStockException
import org.store.clothstar.common.error.exception.order.OrderNotFoundException
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.Seller
import org.store.clothstar.member.service.AddressService
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.member.service.SellerService
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.OrderDetailDTO
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.dto.request.AddOrderDetailRequest
import org.store.clothstar.order.dto.request.OrderRequestWrapper
import org.store.clothstar.order.dto.response.OrderResponse
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
    private val memberService: MemberService,
    private val addressService: AddressService,
    private val sellerService: SellerService,
    private val productService: ProductService,
    private val itemService: ItemService,
) {
    @Transactional(readOnly = true)
    fun getOrder (orderId: String): OrderResponse {
        // orderId 관련 order, member, address 불러오기
        val order: Order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        val member: Member = memberService.getMemberByMemberId(order.memberId)
        val address: Address = addressService.getAddressById(order.addressId)
        val seller: Seller = sellerService.getSellerById(order.memberId)

        // 응답 DTO 생성(주문상세 리스트는 빈 상태)
        val orderResponse = OrderResponse.from(order,member,address)

        // 주문으로부터 주문상세 리스트 가져오기
        val orderDetails: List<OrderDetail> = order.orderDetails
            .filter{ it.deletedAt == null }

        // 주문상세 리스트로부터 productId/itemId 리스트 가져오기
        val productIds: List<Long> = orderDetails.map{ it.productId }
        val itemIds: List<Long> = orderDetails.map{ it.itemId }

        // productIds, itemIds로부터 Product/Item 리스트 가져오기
        val products: List<Product> = productService.findByProductIdIn(productIds)
        val items: List<Item> = itemService.findByIdIn(itemIds)

        // Id, Entity를 Map으로 만들기
        val productMap: Map<Long, Product> = products.associateBy{ it.productId!! }
        val itemMap: Map<Long, Item>  = items.associateBy{ it.itemId!! }

        // Map으로부터 Id, Entity를 가져오면서 주문상세 DTO 리스트 만들기
        val orderDetailDTOList: List<OrderDetailDTO> = orderDetails.map{
            val product: Product = productMap[ it.productId ]
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
            val item: Item = itemMap[ it.itemId ]
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found")
            val brandName: String = seller.brandName
            OrderDetailDTO.from(it, item, product, brandName)
        }

        // 응답 DTO에 주문상세 DTO 리스트 추가
        orderResponse.updateOrderDetailList(orderDetailDTOList)

        // 응답 DTO 반환
        return orderResponse;
    }

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