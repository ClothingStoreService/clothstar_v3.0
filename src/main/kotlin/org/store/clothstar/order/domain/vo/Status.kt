package org.store.clothstar.order.domain.vo

enum class Status {
    CONFIRMED, // 입금확인
    PROCESSING, // 출고처리중(=주문승인)
    CANCELED, // 주문취소
    SHIPPING, // 배송중
    DELIVERED, // 배송완료
    COMPLETED // 구매확정
}