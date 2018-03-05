package com.jay.registry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
@EnableEurekaServer
class RegistryApplication

fun main(args: Array<String>) {
    val dateFormat = SimpleDateFormat("HHmmssSSS")
    System.setProperty("current.date", dateFormat.format(Date()))

    runApplication<RegistryApplication>(*args)
}
