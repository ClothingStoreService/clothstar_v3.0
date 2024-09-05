package org.store.clothstar.member.util

import org.store.clothstar.order.domain.Order
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product

class OrderComponent(
    val order: Order,
    val product: Product,
    val item: Item
)