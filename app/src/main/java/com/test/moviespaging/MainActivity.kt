package com.test.moviespaging

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.moviespaging.data.local.dao.CryptoDao
import com.test.moviespaging.domain.model.Crypto
import com.test.moviespaging.presentation.CryptoViewModel
import com.test.moviespaging.presentation.adapter.CryptoAdapter
import com.test.moviespaging.presentation.adapter.CryptoRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: CryptoViewModel by viewModels()
    private var cryptoAdapter = CryptoAdapter()
    private lateinit var cryptoRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cryptoRecyclerView = findViewById(R.id.crypto_list)
        cryptoRecyclerView.adapter = cryptoAdapter

        cryptoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                val s = cryptoAdapter.itemCount
                if (s - lastItemPosition <= THRESHOLD) {
                    viewModel.loadingMore()
                }
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.cryptoList.collect { state ->
                cryptoAdapter.submitList(state.data)
            }
        }

    }

    companion object {
        const val THRESHOLD = 5
    }
}
