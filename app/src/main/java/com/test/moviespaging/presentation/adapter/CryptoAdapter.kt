package com.test.moviespaging.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.test.moviespaging.R
import com.test.moviespaging.domain.model.Crypto


class CryptoAdapter : ListAdapter<Crypto, CryptoViewHolder>(CryptoDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.crypto_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

