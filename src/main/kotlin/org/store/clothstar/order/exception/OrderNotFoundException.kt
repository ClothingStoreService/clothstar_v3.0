package org.store.clothstar.order.exception

class OrderNotFoundException(val errorCode: OrderErrorCode): RuntimeException(errorCode.message){
}