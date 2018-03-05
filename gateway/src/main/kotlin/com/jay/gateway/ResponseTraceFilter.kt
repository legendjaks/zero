package com.jay.gateway

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.LoggerFactory
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import java.io.InputStream
import java.nio.charset.Charset

@Component
class ResponseTraceFilter : ZuulFilter() {

    private val utf = "UTF-8"

    override fun run(): Any? {
        try {
            trace()
        } catch (e: Exception) {
            log.error("${e.message}")
        }

        return null
    }

    override fun shouldFilter(): Boolean {
        val ctx = RequestContext.getCurrentContext()
        return (ctx.response.status in 400..599)
    }

    override fun filterType() = FilterConstants.POST_TYPE

    override fun filterOrder() = FilterConstants.SEND_RESPONSE_FILTER_ORDER - 10

    private fun trace(){

        val ctx = RequestContext.getCurrentContext()
        if(ctx.response.status == 401)
            return

        val entity = ctx["requestEntity"]
        var req = if(entity != null) (entity as InputStream) else ctx.request.getInputStream()
        var req_body = StreamUtils.copyToString(req, Charset.forName(utf))

        var res_body = ""
        val res = ctx.responseDataStream
        if(res != null) {
            val buf = res.readBytes()
            res_body = String(buf, 0, buf.size)
            ctx.responseBody = res_body
        }

        val params = if(ctx.request.queryString != null) "?${ctx.request.queryString}"  else ""

        log.error("|${ctx.request.method} ${ctx.request.requestURI}$params|${ctx.response.status}| $req_body -> $res_body|")
    }

    companion object {
        private val log = LoggerFactory.getLogger(ResponseTraceFilter::class.java)
    }
}
