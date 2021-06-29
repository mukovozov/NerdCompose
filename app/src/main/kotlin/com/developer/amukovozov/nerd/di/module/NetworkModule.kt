package com.developer.amukovozov.nerd.di.module

import com.developer.amukovozov.nerd.network.NerdApi
import com.developer.amukovozov.nerd.network.adapter.FeedTypeAdapter
import com.developer.amukovozov.nerd.network.adapter.SocialMediaLinkTypeAdapter
import com.developer.amukovozov.nerd.network.interceptor.TokenInterceptor
import com.developer.amukovozov.nerd.repository.UserDataRepository
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
        .add(SocialMediaLinkTypeAdapter())
        .build()

    @Provides
    @Singleton
    fun provideOkhttpClient(userDataRepository: UserDataRepository): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(userDataRepository))
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
