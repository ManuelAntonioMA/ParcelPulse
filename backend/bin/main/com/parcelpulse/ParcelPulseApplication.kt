package com.parcelpulse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParcelPulseApplication

fun main(args: Array<String>) {
    runApplication<ParcelPulseApplication>(*args)
}