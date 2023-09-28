package com.test.moviespaging.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.moviespaging.data.local.dao.CryptoDao
import com.test.moviespaging.data.local.entity.CryptoEntity


@Database(
    entities = [CryptoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoDataBase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao

    companion object {
        private var cryptoDataBase: CryptoDataBase? = null

        fun getTodoDataBaseInstance(context: Context): CryptoDataBase {
            return if (cryptoDataBase == null) {
                synchronized(this) {
                    Room.databaseBuilder(
                        context,
                        CryptoDataBase::class.java,
                        "TodoDataBase"
                    ).fallbackToDestructiveMigration().build()
                }
            } else {
                cryptoDataBase!!
            }
        }
    }
}