package com.test.moviespaging.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.moviespaging.data.local.entity.CryptoEntity

@Dao
interface CryptoDao {
    @Query("SELECT * FROM crypto ORDER BY id LIMIT :limit OFFSET :start")
    fun getCryptoList(start: Int, limit: Int): List<CryptoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoEntity(cryptoEntity: CryptoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoEntityList(cryptoEntityList: List<CryptoEntity>)

    @Delete
    suspend fun deleteCryptoEntity(cryptoEntity: CryptoEntity)

    @Delete
    suspend fun deleteCryptoEntityList(cryptoEntityList: List<CryptoEntity>)

    @Query("DELETE FROM crypto")
    suspend fun clearAllCrypto()
}