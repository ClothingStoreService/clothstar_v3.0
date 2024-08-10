package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.Member
import org.store.clothstar.order.domain.Order
import org.store.clothstar.order.dto.request.CreateOrderRequest

@Service
class OrderCreaterImpl(): OrderCreater {
    override fun createOrder(request: CreateOrderRequest, member: Member, address: Address): Order{
        return request.toOrder(member, address)
    }
}