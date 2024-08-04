package org.store.clothstar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing //Jpa Auditing 기능 활성화
class ClothstarApplication {
}

fun main(args: Array<String>) {
    runApplication<ClothstarApplication>(*args)
}