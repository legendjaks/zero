package com.jay.edge

import com.netflix.client.ClientException
import com.netflix.hystrix.exception.HystrixRuntimeException
import org.apache.commons.lang.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.RuntimeException

@RestControllerAdvice
class ServiceExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val log = LoggerFactory.getLogger(ServiceExceptionHandler::class.java)
    }

    private val glitch = "Sorry.  We are facing a technical glitch. Please try after some time."

    @ExceptionHandler(value = [RuntimeException::class, ProxyException::class, ClientException::class, HystrixRuntimeException::class])
    fun handle(ex: Exception): ResponseEntity<Any> {

        if (ex.cause != null)
            return handleExceptions(ex.cause!!)

        return handleExceptions(ex)
    }

    private fun handleExceptions(ex: Throwable): ResponseEntity<Any> {

        log.error("SEH", causes(ex))

        if (ex is ProxyException) {
            log.info("PE: ${ex.content}")
            return ResponseEntity(ex.content, HttpStatus.valueOf(ex.status_code))
        }

        if (ex is ClientException) {
            log.info("CE: ${ex.message}")
            if (ex.message != null && ex.message!!.contains("Load balancer")) {
                return ResponseEntity( "Service unavailable. Please try after some time.", HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }

        return error()
    }

    @ExceptionHandler(value = [Exception::class, Throwable::class])
    fun handleOthers(ex: Exception): Any? {

        return if (ExceptionUtils.getRootCauseMessage(ex).toLowerCase().contains("broken pipe")) {
            null  //socket is closed, cannot return any response
        } else {
            log.error("SEHU", ex)
            error()
        }
    }

    private fun error(): ResponseEntity<Any> = ResponseEntity(glitch, HttpStatus.INTERNAL_SERVER_ERROR)

    fun causes(throwable: Throwable?): String{

        if(throwable == null)
            return ""

        return throwable.message + " -> " + causes(throwable.cause)
    }
}
