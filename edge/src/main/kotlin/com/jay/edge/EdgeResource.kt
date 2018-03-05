package com.jay.edge

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EdgeResource {

    private val log = LoggerFactory.getLogger(EdgeResource::class.java)

    private val id = Random().nextInt()
    private val version = 1

    @Autowired
    private lateinit var microProxy: MicroProxy

    @GetMapping("/info", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun info(@RequestParam("seq") seq: Int): ResponseEntity<Any> {

        val start = System.currentTimeMillis()

        log.info("Seq: $seq V: $version ID: $id")

        when {
            seq in 400..599 -> return ResponseEntity.status(seq).build()

            seq in 600..50000 -> sleep(seq * 1L)
        }

        val end = System.currentTimeMillis()

        return ResponseEntity.ok(Info(id, version, seq, start, end, end - start))
    }

    @GetMapping("/micro/info", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun microInfo(@RequestParam("seq") seq: Int): ResponseEntity<MicroInfo> {

        val start = System.currentTimeMillis()

        log.info("Seq: $seq V: $version ID: $id")

        val micro = microProxy.info(seq)
        val end = System.currentTimeMillis()

        return ResponseEntity.ok(
                MicroInfo(edge = Info(id, version, seq, start, end, end - start), micro = micro)
        )
    }

    fun sleep(duration: Long) {
        Thread.sleep(duration)
    }
}
