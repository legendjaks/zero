package com.jay.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.exception.ZuulException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.util.concurrent.TimeoutException

@Component
class ZuulErrorFilter : ZuulFilter() {

    val throwable = "throwable"

    val timeout = "Request timeout. Please try after some time"

    override fun run(): Any? {
        try {
            val ctx = RequestContext.getCurrentContext()

            val e = ctx.get(throwable)
            if (e != null && e is ZuulException) {
                log.error("Cause: " + causes(e))
                // remove the error
                ctx.remove(throwable)

                val error = ErrorResponse("Internal Server Error", message = "Sorry.  We are facing a technical glitch. Please try after some time.")
                if(isTimeout(e))
                    error.message = timeout

                val mapper = ObjectMapper();

                ctx.responseStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
                ctx.response.contentType = MediaType.APPLICATION_JSON_VALUE
                ctx.responseBody = mapper.writeValueAsString(error)
            }
        } catch (e: Exception) {
            log.error("Error while handling error")
            ReflectionUtils.rethrowException(e)
        }

        return null
    }

    fun isTimeout(throwable: Throwable?): Boolean {

        if(throwable == null)
            return false

        if(throwable is TimeoutException)
            return true

        return isTimeout(throwable.cause)
    }

    fun causes(throwable: Throwable?): String{

        if(throwable == null)
            return ""

        return throwable.message + " -> " + causes(throwable.cause)
    }

    override fun shouldFilter() = true

    override fun filterType() = "error"

    override fun filterOrder() = Int.MIN_VALUE

    companion object {
        private val log = LoggerFactory.getLogger(ZuulErrorFilter::class.java)
    }
}
