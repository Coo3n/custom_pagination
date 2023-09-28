package com.test.moviespaging.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto")
data class CryptoEntity(
    @PrimaryKey val id: Int, // ID
    val name: String, // ETHERIUM
    val symbol: String, // ETH
    val cmcRank: Int, // 2 RANK
    val dateAdded: String, // DATA
    val lastUpdated: String, // DATA
    val price: Double, // PRICA
    val percentChange: Double, // CHANGE PRICE 1H
)
