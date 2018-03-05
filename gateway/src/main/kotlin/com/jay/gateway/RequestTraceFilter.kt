package com.jay.gateway

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.LoggerFactory
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants
import org.springframework.stereotype.Component

@Component
class RequestTraceFilter : ZuulFilter() {

    private val utf = "UTF-8"

    override fun run(): Any? {
        try {
            trace()
        } catch (e: Exception) {
            log.error("${e.message}")
        }

        return null
    }

    override fun shouldFilter() = true

    override fun filterType() = FilterConstants.PRE_TYPE

    override fun filterOrder() = 1

    private fun trace(){

        val ctx = RequestContext.getCurrentContext()
        val params = if(ctx.request.queryString != null) "?${ctx.request.queryString}"  else ""
        log.error("|${ctx.request.method} ${ctx.request.requestURI}$params|")
    }

    companion object {
        private val log = LoggerFactory.getLogger(RequestTraceFilter::class.java)
    }
}
