package org.store.clothstar.common.config.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*


@Service
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, String>
) {
    @Value("\${spring.data.redis.duration}")
    private var duration: Int = 0

    fun getData(key: String): String {

        val valueOperations = redisTemplate.opsForValue()
        return valueOperations[key]!!
//        return redisTemplate.opsForValue()[key]?.let { it }
//            ?: throw IllegalArgumentException("조회된 redis 데이터가 없습니다.")
    }

    fun existData(key: String): Boolean {
        return java.lang.Boolean.TRUE == redisTemplate.hasKey(key)
    }

    fun setDataExpire(key: String, value: String) {
        val valueOperations = redisTemplate.opsForValue()
        val expireDuration = Duration.ofSeconds(duration.toLong())
        valueOperations[key, value] = expireDuration
    }

    fun deleteData(key: String) {
        redisTemplate.delete(key)
    }

    fun createRedisData(toEmail: String, code: String) {
        if (existData(toEmail)) {
            deleteData(toEmail)
        }

        setDataExpire(toEmail, code)
    }

    fun createdCertifyNum(): String {
        val leftLimit = 48 // number '0'
        val rightLimit = 122 // alphabet 'z'
        val targetStringLength = 6
        val random = Random()

        return random.ints(leftLimit, rightLimit + 1)
            .filter { i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
            .limit(targetStringLength.toLong())
            .collect(
                { StringBuilder() },
                { sb, codePoint -> sb.appendCodePoint(codePoint) },
                { sb1, sb2 -> sb1.append(sb2) }
            )
            .toString()
    }
}