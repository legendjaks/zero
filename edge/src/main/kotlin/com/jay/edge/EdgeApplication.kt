package com.jay.edge

import feign.Logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
class EdgeApplication

@Configuration
class FeignLoggingConfiguration {
    @Bean
    fun feignLoggerLevel() = Logger.Level.FULL
}

fun main(args: Array<String>) {
    val dateFormat = SimpleDateFormat("HHmmssSSS")
    System.setProperty("current.date", dateFormat.format(Date()))

    runApplication<EdgeApplication>(*args)
}
