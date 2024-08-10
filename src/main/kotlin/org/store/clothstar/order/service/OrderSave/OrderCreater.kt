package org.store.clothstar.order.service.OrderSave

import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.dto.request.CreateOrderRequest

interface OrderCreater {
    fun createOrder(request: CreateOrderRequest, member: Member, address: Address): Order
}