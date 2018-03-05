package com.jay.edge

import feign.Response
import feign.Util
import feign.codec.ErrorDecoder
import org.springframework.stereotype.Component

@Component
class FeignErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String, response: Response): Exception {

        val body = if (response.body() != null) Util.toString(response.body().asReader()) else ""
        return ProxyException(response.status(), body)
    }
}
