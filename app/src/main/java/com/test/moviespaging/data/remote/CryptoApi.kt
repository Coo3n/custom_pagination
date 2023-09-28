package com.test.moviespaging.data.remote

import com.test.moviespaging.data.remote.dto.CryptoJsonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {
    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getCryptoList(
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<CryptoJsonList>
}