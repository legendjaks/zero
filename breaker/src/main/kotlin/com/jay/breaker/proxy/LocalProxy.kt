package com.jay.breaker.proxy

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LocalProxy {

    @GET("/edge/info")
    fun info(@Query("seq") seq: Int): Observable<Any>

    @POST("/edge/micro/info")
    fun microInfo(@Query("seq") seq: Int): Observable<Any>
}
