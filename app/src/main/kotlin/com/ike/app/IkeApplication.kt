package com.ike.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IkeApplication

fun main(args: Array<String>) {
    runApplication<IkeApplication>(*args)
}
