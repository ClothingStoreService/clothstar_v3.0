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
                telNo = "010-1234-4444",
                name = "현수",
                memberShoppingActivity = MemberShoppingActivity.init()
            )
        }

        fun getAddress(member: Member): Address {
            return Address(
                receiverName = "현수",
                telNo = "010-1234-4444",
                memberId = member.memberId!!,
                deliveryRequest = "문 앞에 놔주세요",
                addressInfo = AddressInfo.init()
            )
        }

        fun getSeller(member: Member): Seller {
            return Seller(
                memberId = member.memberId!!,
                brandName = "나이키",
                bizNo = "123-123",
                totalSellPrice = 1000
            )
        }

        fun getCategory(): Category {
            return Category(
                categoryType = "상의",
            )
        }

        fun getProduct(member: Member,category: Category): Product {
            return Product(
                memberId = member.memberId!!,
                categoryId = category.categoryId!!,
                name = "상품",
                content = "상품내용",
                price = 1000,
                saleCount = 0,
                displayStatus = DisplayStatus.VISIBLE,
                saleStatus = SaleStatus.ALL,
            )
        }

        fun getItem(product: Product): Item {
            return Item(
                name = "상품 옵션 이름",
                finalPrice = 2000,
                stock = 10,
                saleStatus = SaleStatus.ALL,
                displayStatus = DisplayStatus.HIDDEN,
                product = product
            )
        }

        fun getOrder(member: Member,address: Address): Order {
            return Order(
                orderId = "0eb44b79-6b9a-4ca9-8984-761e18101511",
                memberId = member.memberId!!,
                addressId = address.addressId!!,
                status = Status.CONFIRMED,
                paymentMethod = PaymentMethod.CARD,
                totalPrice = TotalPrice(shipping = 0, products = 0, payment = 0)
            )
        }

        fun getOrderDetail(product: Product,item: Item,order: Order): OrderDetail {
            return OrderDetail(
                productId = product.productId!!,
                itemId = item.itemId!!,
                quantity = 1,
                price = Price(
                    fixedPrice = product.price,
                    oneKindTotalPrice = 10000
                ),
                order = order
            )
        }
    }
}