package com.test.moviespaging.domain.repository

import androidx.paging.PagingData
import com.test.moviespaging.domain.model.Crypto
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getCryptoList(): Flow<PagingData<Crypto>>
}