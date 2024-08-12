package org.store.clothstar.order.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.common.error.ErrorCode
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
    fun getConfirmedOrders(): List<OrderResponse> {
        val confirmedOrders: List<Order> = orderRepository.findConfirmedAndNotDeletedOrders()

        return confirmedOrders.map{
            // order 관련 member, address, seller 불러오기
            val member: Member = memberService.getMemberByMemberId(it.memberId)
            val address: Address = addressService.getAddressById(it.addressId)
            val seller: Seller = sellerService.getSellerById(it.memberId)

            // 응답 DTO 생성(주문상세 리스트는 빈 상태)
            val orderResponse = OrderResponse.from(it,member,address)

            // 주문으로부터 주문상세 리스트 가져오기
            val orderDetails: List<OrderDetail> = it.orderDetails
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
            orderResponse
        }
    }

    @Transactional
    fun approveOrder(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusCONFIRMEDAndDeletedAt()
        order.updateStatus(Status.PROCESSING)
    }

    @Transactional
    fun cancelOrder(orderId: String) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
        order.validateForStatusCONFIRMEDAndDeletedAt()
        order.updateStatus(Status.CANCELED)
    }
}
