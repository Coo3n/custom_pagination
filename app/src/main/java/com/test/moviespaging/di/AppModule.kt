package com.test.moviespaging.di

import android.content.Context
import com.test.moviespaging.data.local.CryptoDataBase
import com.test.moviespaging.data.local.dao.CryptoDao
import com.test.moviespaging.data.remote.CryptoApi
import com.test.moviespaging.data.remote.interceptor.CryptoInterceptor
import com.test.moviespaging.data.repository.CryptoLoader
import com.test.moviespaging.data.repository.CryptoRepositoryImpl
import com.test.moviespaging.domain.repository.CryptoRepository
import com.test.moviespaging.domain.repository.Loader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Reusable
    @Binds
    fun provideCryptoRepository(cryptoRepository: CryptoRepositoryImpl): CryptoRepository

    companion object {
        @Singleton
        @Provides
        fun provideGsonConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }

        @Singleton
        @Provides
        fun provideCryptoInterceptor(): CryptoInterceptor = CryptoInterceptor()

        @Singleton
        @Provides
        fun provideOkHttpClient(cryptoInterceptor: CryptoInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(cryptoInterceptor)
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build()
        }

        @Singleton
        @Provides
        fun provideCryptoApi(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory
        ): CryptoApi {
            return Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(CryptoApi::class.java)
        }

        @Singleton
        @Provides
        fun provideCryptoDataBase(@ApplicationContext context: Context): CryptoDataBase {
            return CryptoDataBase.getTodoDataBaseInstance(context)
        }

        @Singleton
        @Provides
        fun provideCryptoDao(dataBase: CryptoDataBase): CryptoDao {
            return dataBase.cryptoDao()
        }

        @Provides
        @Singleton
        fun provideCryptoLoader(
            cryptoApi: CryptoApi,
            cryptoDao: CryptoDao
        ): CryptoLoader {
            return CryptoLoader(cryptoApi, cryptoDao)
        }
    }
}