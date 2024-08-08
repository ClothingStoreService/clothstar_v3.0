package org.store.clothstar.common

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JasyptConfigTest {

    @Test
    fun jasypt() {
        val url = "jdbc:mysql://clothstar-v83.cpmu4ewuelxk.ap-northeast-2.rds.amazonaws.com/clothstar83"
        val username = ""
        val password = ""

        println(jasyptEncoding(url))
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