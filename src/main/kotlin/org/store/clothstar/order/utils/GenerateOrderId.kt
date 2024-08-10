package org.store.clothstar.order.utils

import java.util.UUID

class GenerateOrderId() {
    companion object{
        fun generateOrderId(): String {
            return UUID.randomUUID().toString()
        }
    }
}