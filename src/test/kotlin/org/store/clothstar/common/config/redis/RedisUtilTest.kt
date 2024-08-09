package org.store.clothstar.common.config.redis

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class RedisUtilTest(
    @Autowired
    private val redisUtil: RedisUtil,
) {
    private val key = "test@test.com"
    val value = "aaa111"

    @BeforeEach
    @DisplayName("redis 데이터가 생성후 확인한다.")
    fun redisCreateData() {
        //given & when
        redisUtil.setDataExpire(key, value)

        //redis 데이터가 생성됐는지 확인한다.
        //then
        assertTrue(redisUtil.existData(key))
        assertEquals(redisUtil.getData(key), value)
    }

    @DisplayName("redis 데이터를 조회한다.")
    @Test
    fun redisGetDate() {
        //when
        val value = redisUtil.getData(key)

        //then
        Assertions.assertThat(value).isEqualTo(value)
    }

    @DisplayName("redis 데이터가 삭제되는지 확인한다.")
    @Test
    @Throws(Exception::class)
    fun redisCreateAndDeleteTest() {
        //when
        redisUtil.deleteData(key)

        //then
        assertFalse(redisUtil.existData(key))
    }
}