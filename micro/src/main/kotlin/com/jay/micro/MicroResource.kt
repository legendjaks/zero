package com.jay.micro

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/micro")
class MicroResource {

    private val log = LoggerFactory.getLogger(MicroResource::class.java)

    private val id = Random().nextInt()
    private val version = 1

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun version(@RequestParam("seq") seq: Int): ResponseEntity<Any> {

        val start = System.currentTimeMillis()

        log.info("Seq: $seq V: $version ID: $id")

        if(version == 1) {
            when {
                seq in 400..599 -> return ResponseEntity.status(seq).build()

                seq in 600..50000 -> sleep(seq * 1L)
            }
        }else{
            when {
                seq in 400..599 -> return ResponseEntity.status(seq).build()
            }
        }

        val end = System.currentTimeMillis()

        return ResponseEntity.ok(Info(id, version, seq, start, end, end - start))
    }

    fun sleep(duration: Long) {
        Thread.sleep(duration)
    }
}
