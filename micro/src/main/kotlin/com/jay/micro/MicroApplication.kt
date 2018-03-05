package com.jay.micro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
@EnableDiscoveryClient
class OrdersApplication

fun main(args: Array<String>) {
    val dateFormat = SimpleDateFormat("HHmmssSSS")
    System.setProperty("current.date", dateFormat.format(Date()))

    runApplication<OrdersApplication>(*args)
}
