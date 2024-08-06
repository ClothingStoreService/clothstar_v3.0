package org.store.clothstar.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    var status: HttpStatus,
    var message: String,
) {
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "멤버 정보를 찾을 수 없습니다."),

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일 입니다."),
    DUPLICATED_SELLER(HttpStatus.BAD_REQUEST, "이미 판매자 가입이 되어 있습니다."),
    DUPLICATED_BIZNO(HttpStatus.BAD_REQUEST, "이미 존재하는 사업자 번호 입니다."),
    DUPLICATED_BRAND_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 브랜드 이름 입니다."),
    DUPLICATED_TEL_NO(HttpStatus.BAD_REQUEST, "이미 존재하는 핸드폰 번호 입니다."),

    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "refresh 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refresh 토큰이 만료되었거나 유효하지 않습니다."),
    INVALID_AUTH_CERTIFY_NUM(HttpStatus.BAD_REQUEST, "인증번호가 잘못 되었습니다."),

    // Order 관련 에러코드
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "존재하지 않는 주문번호입니다."),
    INVALID_ORDER_STATUS_CONFIRMED(HttpStatus.BAD_REQUEST, "주문이 입금확인 상태가 아니므로 요청을 처리할 수 없습니다.");

    fun ErrorCode(status: HttpStatus, message: String) {
        this.status = status
        this.message = message
    }
}