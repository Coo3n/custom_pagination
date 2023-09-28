package com.test.moviespaging.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class CryptoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("X-CMC_PRO_API_KEY", TOKEN)
        return chain.proceed(request.build())
    }

    companion object {
        const val TOKEN = "bd721070-bc05-46b7-8536-4d4f677c7451"
    }
}