package com.test.moviespaging.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.moviespaging.data.remote.CryptoApi
import com.test.moviespaging.data.remote.CryptoListPagingSource
import com.test.moviespaging.domain.model.Crypto
import com.test.moviespaging.domain.repository.CryptoRepository
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val cryptoApi: Lazy<CryptoApi>
) : CryptoRepository {
    override fun getCryptoList(): Flow<PagingData<Crypto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CryptoListPagingSource(cryptoApi)
            }
        ).flow
    }
}