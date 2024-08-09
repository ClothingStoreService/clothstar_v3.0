package org.store.clothstar.order.utils

import java.security.SecureRandom
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GenerateOrderId {
    companion object {
        val DATE_FORMAT: String = "yyyyMMdd"
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val RANDOM_NUMBER_LENGTH: Int = 7
        val SECURE_RANDOM: SecureRandom = SecureRandom()

        // 주문생성날짜와 랜덤숫자를 이용하여 주문번호를 생성한다.
        fun generateOrderId(): Long {
            val datePrefix: String = getDatePrefix()
            val randomDigits: String = generateRandomDigits()
            return (datePrefix + randomDigits).toLong()
        }

        // 특정 날짜 형식으로 바꾼 현재 날짜를 얻는다.
        fun getDatePrefix(): String {
            return LocalDate.now().format(DATE_TIME_FORMATTER)
        }

        // 특정 개수의 String타입 랜덤 숫자를 생성한다.
        fun generateRandomDigits(): String {
            return (0 until RANDOM_NUMBER_LENGTH)
                .map { SECURE_RANDOM.nextInt(10).toString() }
                .joinToString("")
        }
    }
}