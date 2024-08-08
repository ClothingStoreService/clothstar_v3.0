//package org.store.clothstar.order.service
//
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.Pageable
//import org.springframework.data.domain.Slice
//import org.springframework.http.HttpStatus
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//import org.springframework.web.server.ResponseStatusException
//import org.store.clothstar.common.error.ErrorCode
//import org.store.clothstar.common.error.exception.order.OrderNotFoundException
//import org.store.clothstar.member.service.AddressService
//import org.store.clothstar.member.service.MemberService
//import org.store.clothstar.order.domain.Order
//import org.store.clothstar.order.domain.OrderDetail
//import org.store.clothstar.order.domain.vo.OrderDetailDTO
//import org.store.clothstar.order.dto.request.OrderRequestWrapper
//import org.store.clothstar.order.dto.response.OrderResponse
//import org.store.clothstar.order.repository.OrderDetailRepository
//import org.store.clothstar.order.repository.OrderRepository
//import org.store.clothstar.product.domain.Item
//import org.store.clothstar.product.domain.Product
//import org.store.clothstar.product.repository.ItemRepository
//import org.store.clothstar.product.service.ProductService
//import java.util.function.Consumer
//import java.util.function.Function
//import java.util.stream.Collectors
//
//@Service
//class OrderService(
//    private val orderRepository: OrderRepository,
//    private val memberService: MemberService,
//    private val addressService: AddressService,
//    private val orderDetailService: OrderDetailService,
//    private val orderDetailRepository: OrderDetailRepository,
//    private val productService: ProductService,
//    private val itemRepository: ItemRepository
////    private val itemService: ItemService,
////    private val orderSaveFacade: OrderSaveFacade
//) {
//
//    @Transactional(readOnly = true)
//    fun getOrder(orderId: Long) : OrderResponse {
//        val order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
//            ?: throw OrderNotFoundException(ErrorCode.NOT_FOUND_ORDER)
//
//        val member = memberService.getMemberByMemberId(order.memberId)
//        val address = addressService.getAddressById(order.addressId)
//
//        val orderResponse = OrderResponse.from(order,member,address)
//
//        val orderDetails : List<OrderDetail> = order.orderDetails.filter{ it.deletedAt == null }
//        val itemIds : List<Long> = orderDetails.map{ it.itemId }
//        val productIds : List<Long> = orderDetails.map { it.productId }
//
//        val items = itemRepository.findByItemIdIn(itemIds)
//        val products = productService.findByIdIn(productIds)
//
//        val itemMap : Map<Long, Item> = items.stream().collect(Collectors.toMap(Item::itemId, item -> item))
//        val productMap : Map<Long, Product> = products.stream().collect(Collectors.toMap(ProductLine::getProductLineId, productLine -> productLine))
//
//        List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
//            Product product = productMap.get(orderDetail.getProductId());
//            ProductLine productLine = productLineMap.get(orderDetail.getProductLineId());
//            return OrderDetailDTO.from(orderDetail, product, productLine);
//        }).collect(Collectors.toList());
//
//        orderResponse.updateOrderDetailList(orderDetailDTOList);
//
//        return orderResponse;
//    }
//
//    public Page<OrderResponse> getAllOrderOffsetPaging(Pageable pageable) {
//        Page<Order> orderEntities = orderUserRepository.findAll(pageable);
//
//        return orderEntities.map(order -> {
//            Member member = memberService.getMemberByMemberId(order.getMemberId());
//            Address address = addressService.getAddressById(order.getAddressId());
//            OrderResponse orderResponse = OrderResponse.from(order, member, address);
//
//            List<OrderDetail> orderDetails = order.getOrderDetails().stream()
//                .filter(orderDetail -> orderDetail.getDeletedAt() == null)
//            .toList();
//            List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
//            List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());
//
//            List<Product> products = productService.findByIdIn(productIds);
//            List<ProductLine> productLines = productLineService.findByIdIn(productLineIds);
//
//            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getProductId, product -> product));
//            Map<Long, ProductLine> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLine::getProductLineId, productLine -> productLine));
//
//            List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
//            Product product = productMap.get(orderDetail.getProductId());
//            ProductLine productLine = productLineMap.get(orderDetail.getProductLineId());
//            return OrderDetailDTO.from(orderDetail, product, productLine);
//        }).collect(Collectors.toList());
//
//            orderResponse.setterOrderDetailList(orderDetailDTOList);
//
//            return orderResponse;
//        });
//    }
//
//    public Slice<OrderResponse> getAllOrderSlicePaging(Pageable pageable) {
//        Slice<Order> orderEntities = orderUserRepository.findAll(pageable);
//
//        return orderEntities.map(order -> {
//            Member member = memberService.getMemberByMemberId(order.getMemberId());
//            Address address = addressService.getAddressById(order.getAddressId());
//            OrderResponse orderResponse = OrderResponse.from(order, member, address);
//
//            List<OrderDetail> orderDetails = order.getOrderDetails().stream()
//                .filter(orderDetail -> orderDetail.getDeletedAt() == null)
//            .toList();
//            List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
//            List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());
//
//            List<Product> products = productService.findByIdIn(productIds);
//            List<ProductLine> productLines = productLineService.findByIdIn(productLineIds);
//
//            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getProductId, product -> product));
//            Map<Long, ProductLine> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLine::getProductLineId, productLine -> productLine));
//
//            List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
//            Product product = productMap.get(orderDetail.getProductId());
//            ProductLine productLine = productLineMap.get(orderDetail.getProductLineId());
//            return OrderDetailDTO.from(orderDetail, product, productLine);
//        }).collect(Collectors.toList());
//
//            orderResponse.setterOrderDetailList(orderDetailDTOList);
//
//            return orderResponse;
//        });
//    }
//
//    @Transactional
//    public Long saveOrder(OrderRequestWrapper orderRequestWrapper) {
//        return orderSaveFacade.saveOrder(orderRequestWrapper);
//    }
//
//    @Transactional
//    public void confirmOrder(Long orderId) {
//
//        Order order = orderUserRepository.findById(orderId)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));
//
//        if (order.getStatus() != Status.DELIVERED) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
//        }
//
//        order.setterStatus(Status.CONFIRM);
//        orderUserRepository.save(order);
//    }
//
//    public void cancelOrder(Long orderId) {
//
//        Order order = orderUserRepository.findById(orderId)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));
//
//        if (order.getStatus() != Status.WAITING && order.getStatus() != Status.APPROVE) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'승인대기' 또는 '주문승인' 상태가 아니기 때문에 주문을 취소할 수 없습니다.");
//        }
//
//        order.setterStatus(Status.CANCEL);
//        orderUserRepository.save(order);
//
//        orderDetailService.restoreStockByOrder(orderId);
//    }
//
//    @Transactional
//    public void updateDeleteAt(Long orderId) {
//        Order order = orderUserRepository.findById(orderId)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다."));
//
//        if(order.getDeletedAt() != null){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 주문입니다.");
//        }
//
//        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);
//        orderDetailList.forEach(OrderDetail::updateDeletedAt);
//
//        order.updateDeletedAt();
//    }
//}