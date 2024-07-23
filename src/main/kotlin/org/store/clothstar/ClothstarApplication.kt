package org.store.clothstar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClothstarApplication

fun main(args: Array<String>) {
	runApplication<ClothstarApplication>(*args)
}