package org.store.clothstar.order.exception

class InvalidOrderStatusException(val errorCode: OrderErrorCode): RuntimeException(errorCode.message) {
}