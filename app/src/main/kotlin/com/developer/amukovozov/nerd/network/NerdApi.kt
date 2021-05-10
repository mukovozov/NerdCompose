package com.developer.amukovozov.nerd.network

import com.developer.amukovozov.nerd.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NerdApi {
    companion object {
        const val BASE_URL = "https://nerd-api-developing.herokuapp.com"
    }

    @POST("/register/oauth")
    fun yandexAuth(@Query("yandexToken") token: String): Single<TokenInfo>

    @GET("/user/me")
    fun getUserMe(): Single<ProfileDetailsResponse>

    @GET("/followings")
    fun getMyFollowings(): Single<List<UserInfo>>

    @GET("/followers")
    fun getMyFollowers(): Single<List<UserInfo>>

    @GET("/feed")
    fun getFeed(@Query("page") page: Int): Single<Pagination<Feed>>

    @POST("/feed/like")
    fun addLike(@Query("feedId") feedId: Int): Completable

    @POST("/feed/like/remove")
    fun removeLike(@Query("feedId") feedId: Int): Completable
}