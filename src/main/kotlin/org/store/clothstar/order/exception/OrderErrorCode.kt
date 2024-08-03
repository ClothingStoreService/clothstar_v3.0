package org.store.clothstar.order.exception

import org.springframework.http.HttpStatus

enum class OrderErrorCode(val status: HttpStatus, val message: String) {
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND,"존재하지 않는 주문번호입니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST,"주문이 승인대기 상태가 아니므로 요청을 처리할 수 없습니다.")
}