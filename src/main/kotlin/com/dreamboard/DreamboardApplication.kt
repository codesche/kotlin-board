package com.dreamboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class DreamboardApplication

fun main(args: Array<String>) {
    runApplication<DreamboardApplication>(*args)
}
