package org.store.clothstar.common.config

import org.springframework.stereotype.Component

@Component
class VaultData {
    //    @Value("\${demo.username}")
    lateinit var username: String

    //    @Value("\${demo.password}")
    lateinit var password: String
}