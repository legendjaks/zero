package com.jay.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
@EnableConfigServer
class ConfigApplication

fun main(args: Array<String>) {
    val dateFormat = SimpleDateFormat("HHmmssSSS")
    System.setProperty("current.date", dateFormat.format(Date()))

    runApplication<ConfigApplication>(*args)
}
