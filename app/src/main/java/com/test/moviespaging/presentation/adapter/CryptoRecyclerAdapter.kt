package com.test.moviespaging.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.test.moviespaging.R
import com.test.moviespaging.domain.model.Crypto

class CryptoRecyclerAdapter :
    PagingDataAdapter<Crypto, CryptoViewHolder>(CryptoDiffUtil) {

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.crypto_item,
                parent,
                false
            )
        )
    }
}

class CryptoViewHolder(itemView: View) : ViewHolder(itemView) {
    private val coinName = itemView.findViewById<TextView>(R.id.name_coin)
    private val coinPrice = itemView.findViewById<TextView>(R.id.price_coin)

    fun bind(crypto: Crypto) {
        coinName.text = crypto.name
        coinPrice.text = crypto.price.toString()
    }
}

object CryptoDiffUtil : DiffUtil.ItemCallback<Crypto>() {
    override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
        return oldItem == newItem
    }

}