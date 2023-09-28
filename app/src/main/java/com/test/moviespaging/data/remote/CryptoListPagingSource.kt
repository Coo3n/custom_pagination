package com.test.moviespaging.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.moviespaging.data.mapper.toCrypto
import com.test.moviespaging.domain.model.Crypto
import dagger.Lazy
import retrofit2.HttpException

class CryptoListPagingSource(
    private val cryptoApi: Lazy<CryptoApi>
) : PagingSource<Int, Crypto>() {
    override fun getRefreshKey(state: PagingState<Int, Crypto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Crypto> {
        val page = params.key ?: INIT_LOAD_PAGE
//        val start = if (params.key != null) {
//            ((position - 1) * NETWORK_PAGE_SIZE) + 1
//        } else {
//            INIT_LOAD_PAGE
//        }

        return try {
            val response = cryptoApi.get().getCryptoList(start = page, limit = params.loadSize)

            if (response.isSuccessful) {
                val cryptoList = checkNotNull(response.body())
                    .cryptoList.map { cryptoJson -> cryptoJson.toCrypto() }

                val nextKey = if (cryptoList.size < params.loadSize) null else page + 1
                val prevKey = if (page == 1) null else page - 1

                LoadResult.Page(
                    data = cryptoList,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val INIT_LOAD_PAGE = 1
        const val NETWORK_PAGE_SIZE = 10
    }
}