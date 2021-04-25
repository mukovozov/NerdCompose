package com.developer.amukovozov.nerd.network

import com.developer.amukovozov.nerd.model.Feed
import com.developer.amukovozov.nerd.model.Pagination
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NerdApi {
    companion object {
        const val BASE_URL = "https://nerd-api-developing.herokuapp.com"
    }

    @GET("/feed")
    fun getFeed(@Query("page") page: Int): Single<Pagination<Feed>>
}