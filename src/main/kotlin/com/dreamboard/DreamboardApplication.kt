package com.dreamboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DreamboardApplication

fun main(args: Array<String>) {
    runApplication<DreamboardApplication>(*args)
}
