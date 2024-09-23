package org.store.clothstar.common.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class VaultDataTest {
    @Value("\${email.send}")
    lateinit var email: String

    @Test
    fun demoTest() {
        println("email : ${email}")
    }
}