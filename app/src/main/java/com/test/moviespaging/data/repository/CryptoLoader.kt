package com.test.moviespaging.data.repository

import com.test.moviespaging.data.local.dao.CryptoDao
import com.test.moviespaging.data.mapper.toCrypto
import com.test.moviespaging.data.mapper.toCryptoEntity
import com.test.moviespaging.data.remote.CryptoApi
import com.test.moviespaging.domain.model.Crypto
import com.test.moviespaging.domain.repository.InnerState
import com.test.moviespaging.domain.repository.Loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class CryptoLoader @Inject constructor(
    private val cryptoApi: CryptoApi,
    private val cryptoDao: CryptoDao
) : Loader<List<Crypto>> {
    // Внутренний стейт с ошибками
    private val _state = MutableStateFlow(InnerState(data = listOf<Crypto>()))

    // Внешний стейт, который маппиться автоматически
    override val state = _state.map { innerState ->
        Loader.State(
            data = innerState.data,
            isLoading = innerState.isLoadingFromCache || innerState.isLoadingFromRemote,
            isError = innerState.remoteError
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class) // Чтобы стейт изменялся в одном потоке
    private val workDispatcher = Dispatchers.IO.limitedParallelism(1)
    private var scope: CoroutineScope = CoroutineScope(workDispatcher)

    private var currentPage = 0

    override fun load() {
        launchWork { state ->
            launchLoad(state)
        }
    }

    private fun launchWork(block: (InnerState<List<Crypto>>) -> InnerState<List<Crypto>>) {
        scope.launch {
            _state.value = block(_state.value)
        }
    }

    private fun launchLoad(state: InnerState<List<Crypto>>): InnerState<List<Crypto>> {
        if (state.hasMoreInCache && !state.isLoadingFromCache) {
            return launchCacheLoad(state = state)
        }

        if (state.hasMoreInRemote
            && !state.isLoadingFromRemote
            && !state.isLoadingFromCache
            && !state.remoteError
        ) {
            return launchRemoteLoad(state = state)
        }

        return state
    }

    private fun launchCacheLoad(
        state: InnerState<List<Crypto>>
    ): InnerState<List<Crypto>> {
        scope.launch(Dispatchers.IO) {
            val cryptoList = cryptoDao.getCryptoList(
                start = currentPage * 30,
                limit = 30
            ).map { cryptoEntity -> cryptoEntity.toCrypto() }

            launchWork { state ->
                onCacheLoaded(
                    data = cryptoList,
                    state = state
                )
            }
        }

        return state.copy(
            isLoadingFromCache = true
        )
    }

    private fun onCacheLoaded(
        data: List<Crypto>,
        state: InnerState<List<Crypto>>
    ): InnerState<List<Crypto>> {
        val mergedList = state.data + data
        val hasMoreInCache = data.size == 30

        currentPage++

        return state.copy(
            data = mergedList,
            isLoadingFromCache = false,
            hasMoreInCache = hasMoreInCache
        )
    }

    private fun launchRemoteLoad(
        state: InnerState<List<Crypto>>
    ): InnerState<List<Crypto>> {
        scope.launch(Dispatchers.IO) {
            try {
                val response = cryptoApi.getCryptoList(
                    start = if (currentPage == 0) {
                        1
                    } else {
                        currentPage * 30
                    },
                    limit = 30
                )

                if (response.isSuccessful) {
                    val cryptoList = checkNotNull(response.body()).cryptoList
                        .map { cryptoJson ->
                            cryptoJson.toCrypto()
                        }

                    cryptoDao.insertCryptoEntityList(cryptoList.map { it.toCryptoEntity() })

                    launchWork { state ->
                        onRemoteLoaded(data = cryptoList, state = state)
                    }
                } else {
                    launchWork { state ->
                        state.copy(
                            isLoadingFromRemote = false,
                            remoteError = true
                        )
                    }
                }
            } catch (httpException: HttpException) {
                launchWork { state ->
                    state.copy(
                        isLoadingFromRemote = false,
                        remoteError = true
                    )
                }
            }
        }

        return state.copy(
            isLoadingFromRemote = true
        )
    }

    private fun onRemoteLoaded(
        data: List<Crypto>,
        state: InnerState<List<Crypto>>
    ): InnerState<List<Crypto>> {
        val mergedData = state.data + data
        val hasMoreInCache = data.size == 30

        currentPage++

        return state.copy(
            data = mergedData,
            remoteError = false,
            isLoadingFromRemote = false,
            hasMoreInRemote = hasMoreInCache
        )
    }
}