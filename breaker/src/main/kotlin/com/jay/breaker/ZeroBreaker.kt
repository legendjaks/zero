package com.jay.breaker

import com.jay.breaker.proxy.LocalProxy
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class ZeroBreaker {

    companion object {

        fun create(): LocalProxy {

            val httpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost:8080")
                .client(httpClient)
                .build()

            return retrofit.create(LocalProxy::class.java)
        }
    }

    private val client = create()

    fun breakIt() {

        for (i in 10000..10010) {
            retrieve(i)
            sleep(300)
        }
    }

    fun retrieve(seq: Int) {

        val start = System.currentTimeMillis()
        client.info(seq).subscribe({
            val end = System.currentTimeMillis()
            println("$seq - ${LocalDateTime.now()} - ${it} - ${end - start}")
        }, {
            val end = System.currentTimeMillis()
            println("$seq - ${LocalDateTime.now()} FAIL: ${it.message} - ${end - start}")
        })
    }

    fun sleep(duration: Long) {
        Thread.sleep(duration)
    }
}

fun main(args: Array<String>) {

    val breaker = ZeroBreaker()
    breaker.breakIt()
}
