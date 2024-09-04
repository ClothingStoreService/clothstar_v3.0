package org.store.clothstar.common.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class VaultDataTest(
    @Autowired private val vaultData: VaultData,
) {
    @Test
    fun demoTest() {
        println("username : ${vaultData.username}")
        println("password : ${vaultData.password}")
    }
}