package com.test.moviespaging.presentation

import androidx.lifecycle.ViewModel
import com.test.moviespaging.data.repository.CryptoLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val loader: CryptoLoader
) : ViewModel() {
    var cryptoList = loader.state

    init {
        loadingMore()
    }

    fun loadingMore() {
        loader.load()
    }
}