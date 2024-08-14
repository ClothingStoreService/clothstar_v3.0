package org.store.clothstar.order.utils

import org.store.clothstar.category.domain.Category
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.domain.OrderDetail
import org.store.clothstar.order.domain.vo.PaymentMethod
import org.store.clothstar.order.domain.vo.Price
import org.store.clothstar.order.domain.vo.Status
import org.store.clothstar.order.domain.vo.TotalPrice
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.SaleStatus

class CreateOrderObject {
    companion object {
        fun getCategory(): Category {
            return Category(
                categoryId = 1L,
                categoryType = "상의",
            )
        }

        fun getProduct(): Product {
            return Product(
                productId = 1L,
                memberId = 1L,
                categoryId = this.getCategory().categoryId!!,
                name = "상품",
                content = "상품내용",
                price = 1000,
                saleCount = 0,
                displayStatus = DisplayStatus.VISIBLE,
                saleStatus = SaleStatus.ALL,
            )
        }

        fun getItem(): Item {
            return Item(
                itemId = 1L,
                name = "상품 옵션 이름",
                finalPrice = 2000,
                stock = 10,
                saleStatus = SaleStatus.ALL,
                displayStatus = DisplayStatus.HIDDEN,
                product = this.getProduct()
            )
        }

        fun getOrder(): Order {
            return Order(
                orderId = "0eb44b79-6b9a-4ca9-8984-761e18101511",
                memberId = 1L,
                addressId = 1L,
                status = Status.CONFIRMED,
                paymentMethod = PaymentMethod.CARD,
                totalPrice = TotalPrice(shipping = 0, products = 0, payment = 0)
            )
        }

        fun getOrderDetail(): OrderDetail {
            return OrderDetail(
                orderDetailId = 1L,
                productId = this.getProduct().productId!!,
                itemId = this.getItem().itemId!!,
                quantity = 1,
                price = Price(fixedPrice = this.getProduct().price,
                    oneKindTotalPrice = 10000),
                order = this.getOrder()
            )
        }
    }
}