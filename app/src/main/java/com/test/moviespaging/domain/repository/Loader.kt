package com.test.moviespaging.domain.repository

import kotlinx.coroutines.flow.Flow

interface Loader<T> {
    class State<T>(
        val data: T,
        val isLoading: Boolean,
        val isError: Boolean
    )

    val state: Flow<State<T>>

    fun load()
}

data class InnerState<T>(
    val data: T,
    val isLoadingFromCache: Boolean = false,
    val isLoadingFromRemote: Boolean = false,
    val hasMoreInCache: Boolean = true,
    val hasMoreInRemote: Boolean = true,
    val remoteError: Boolean = false,
)