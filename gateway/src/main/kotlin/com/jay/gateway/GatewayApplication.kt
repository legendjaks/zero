package com.jay.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
class GatewayApplication

fun main(args: Array<String>) {
    val dateFormat = SimpleDateFormat("HHmmssSSS")
    System.setProperty("current.date", dateFormat.format(Date()))

    runApplication<GatewayApplication>(*args)
}
