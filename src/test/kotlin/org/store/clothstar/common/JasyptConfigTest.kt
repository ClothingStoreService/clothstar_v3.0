package org.store.clothstar.common

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class JasyptConfigTest {

    @Test
    fun jasypt() {
        val url = "T9fJjUMN3RPDMOfr0v3yGHsqf1EnX0FrL1288rJZVZYhjKxeYTZHkg=="
        val username = ""
        val password = ""

        println(jasyptDecoding(url))
        println(jasyptEncoding(username))
        println(jasyptEncoding(password))
    }

    fun jasyptEncoding(value: String): String {
        val key = "my_jasypt_key"
        val pbeEnc = StandardPBEStringEncryptor()

        pbeEnc.setAlgorithm("PBEWithMD5AndDES")
        pbeEnc.setPassword(key)

        return pbeEnc.encrypt(value)
    }

    fun jasyptDecoding(value: String): String {
        val key = "my_jasypt_key"
        val pbeEnc = StandardPBEStringEncryptor()

        pbeEnc.setAlgorithm("PBEWithMD5AndDES")
        pbeEnc.setPassword(key)

        return pbeEnc.decrypt(value)
    }
}