package com.test.moviespaging.domain.model

data class Crypto(
    val id: Int, // ID
    val name: String, // ETHERIUM
    val symbol: String, // ETH
    val cmcRank: Int, // 2 RANK
    val dateAdded: String, // DATA
    val lastUpdated: String, // DATA
    val price: Double, // PRICA
    val percentChange: Double, // CHANGE PRICE 1H
)
