package com.jay.edge

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "http://micro")
interface MicroProxy {

    @RequestMapping("/micro", method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun info(@RequestParam("seq") seq: Int): Info
}
