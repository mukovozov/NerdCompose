package com.developer.amukovozov.nerd.network

import com.developer.amukovozov.nerd.model.Pagination
import com.developer.amukovozov.nerd.model.TokenInfo
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.model.UserInfoDetails
import com.developer.amukovozov.nerd.model.feed.Feed
import com.developer.amukovozov.nerd.model.feed.FeedRequest
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.model.movie.MovieDetails
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
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
    fun getUserMe(): Single<UserInfoDetails>

    @GET("/user")
    fun getUserById(@Query("id") userId: Int): Single<UserInfoDetails>

    @GET("/followings")
    fun getMyFollowings(): Single<List<UserInfo>>

    @GET("/followings")
    fun getUserFollowings(@Query("id") userId: Int): Single<List<UserInfo>>

    @GET("/followers")
    fun getMyFollowers(): Single<List<UserInfo>>

    @GET("/followers")
    fun getUserFollowers(@Query("id") userId: Int): Single<List<UserInfo>>

    @POST("/follow")
    fun follow(@Query("id") userId: Int): Completable

    @POST("/unfollow")
    fun unfollow(@Query("id") userId: Int): Completable

    @GET("/watchlist")
    fun getMyWatchlist(@Query("page") page: Int): Single<Pagination<Movie>>

    @POST("/watchlist/add")
    fun addToWatchlist(@Query("id") movieId: Int): Completable

    @POST("/watchlist/delete")
    fun deleteFromWatchlist(@Query("id") movieId: Int): Completable

    @GET("/watchlist")
    fun getUserWatchlist(@Query("page") page: Int, @Query("userId") userId: Int): Single<Pagination<Movie>>

    @GET("/feed")
    fun getFeed(@Query("page") page: Int): Single<Pagination<Feed>>

    @GET("feed/user/me")
    fun getMyFeed(@Query("page") page: Int): Single<Pagination<Feed>>

    @GET("feed/user")
    fun getUserFeed(@Query("id") id: Int, @Query("page") page: Int): Single<Pagination<Feed>>

    @POST("/feed/like")
    fun addLike(@Query("feedId") feedId: Int): Completable

    @POST("/feed/like/remove")
    fun removeLike(@Query("feedId") feedId: Int): Completable

    @POST("/feed/create")
    fun createFeed(@Body feed: FeedRequest): Completable

    @GET("/movie/details")
    fun getMovieDetails(@Query("id") id: Int): Single<MovieDetails>

    @GET("/movies/search")
    fun searchMovies(@Query("query") query: String): Single<List<Movie>>

    @GET("movie/short")
    fun getShortMovieInfo(@Query("id") id: Int): Single<Movie>

    @GET("tags")
    fun getTags(): Single<List<Tag>>
}