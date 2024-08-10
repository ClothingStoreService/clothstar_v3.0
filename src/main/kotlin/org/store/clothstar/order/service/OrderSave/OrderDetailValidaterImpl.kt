package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.order.InsufficientStockException
import org.store.clothstar.common.error.exception.order.OutOfStockException
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest
import org.store.clothstar.product.domain.Item

@Service
class OrderDetailValidaterImpl(): OrderDetailValidater {
    override fun validateOrder(request: CreateOrderDetailRequest, item: Item) {
        // 상품재고가 0일때 품절 예외처리
        if (item.stock == 0){
            throw OutOfStockException(ErrorCode.OUT_OF_STOCK)
        }

        // 주문개수가 상품재고보다 많을 때 재고 부족 예외처리
        if (request.quantity > item.stock) {
            throw InsufficientStockException(ErrorCode.INSUFFICIENT_STOCK)
        }
    }
}