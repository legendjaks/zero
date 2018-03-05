package com.jay.edge

import feign.FeignException

data class ProxyException(val status_code: Int, val content: String) : FeignException(status_code, content)
