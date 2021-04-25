package com.developer.amukovozov.nerd.di.module

import com.developer.amukovozov.nerd.network.NerdApi
import com.developer.amukovozov.nerd.network.adapter.FeedTypeAdapter
import com.developer.amukovozov.nerd.network.interceptor.TokenInterceptor
import com.developer.amukovozov.nerd.repository.TokenRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(FeedTypeAdapter())
        .build()

    @Provides
    @Singleton
    fun provideOkhttpClient(tokenRepository: TokenRepository): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(tokenRepository))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideNerdApi(client: OkHttpClient, moshi: Moshi): NerdApi {
        return Retrofit.Builder()
            .baseUrl(NerdApi.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NerdApi::class.java)
    }
}
