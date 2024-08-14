package org.store.clothstar.order.util

import org.store.clothstar.category.domain.Category
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.Seller
import org.store.clothstar.member.domain.vo.AddressInfo
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
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
        fun getMember(): Member {
            return Member(
                memberId = 1L,
                telNo = "010-1234-4444",
                name = "현수",
                memberShoppingActivity = MemberShoppingActivity.init()
            )
        }

        fun getAddress(): Address {
            return Address(
                addressId = 1L,
                receiverName = "현수",
                telNo = "010-1234-4444",
                memberId = this.getMember().memberId!!,
                deliveryRequest = "문 앞에 놔주세요",
                addressInfo = AddressInfo.init()
            )
        }

        fun getSeller(): Seller {
            return Seller(
                memberId = this.getMember().memberId!!,
                brandName = "나이키",
                bizNo = "123-123",
                totalSellPrice = 1000
            )
        }

        fun getCategory(): Category {
            return Category(
                categoryId = 1L,
                categoryType = "상의",
            )
        }

        fun getProduct(): Product {
            return Product(
                productId = 1L,
                memberId = this.getMember().memberId!!,
                categoryId = getCategory().categoryId!!,
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
                product = getProduct()
            )
        }

        fun getOrder(): Order {
            return Order(
                orderId = "0eb44b79-6b9a-4ca9-8984-761e18101511",
                memberId = this.getMember().memberId!!,
                addressId = this.getAddress().addressId!!,
                status = Status.CONFIRMED,
                paymentMethod = PaymentMethod.CARD,
                totalPrice = TotalPrice(shipping = 0, products = 0, payment = 0)
            )
        }

        fun getOrderDetail(): OrderDetail {
            return OrderDetail(
                orderDetailId = 1L,
                productId = getProduct().productId!!,
                itemId = getItem().itemId!!,
                quantity = 1,
                price = Price(
                    fixedPrice = getProduct().price,
                    oneKindTotalPrice = 10000
                ),
                order = getOrder()
            )
        }
    }
}