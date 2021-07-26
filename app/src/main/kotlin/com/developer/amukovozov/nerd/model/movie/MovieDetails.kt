package com.developer.amukovozov.nerd.model.movie

import com.developer.amukovozov.nerd.model.feed.CountableTag
import com.developer.amukovozov.nerd.model.feed.Feed
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetails(
    @Json(name = "id")
    val id: Int,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "belongs_to_collection")
    val belongsToCollection: String?,
    @Json(name = "genres")
    val genres: List<Genres>,
    @Json(name = "imdb_id")
    val imdbId: String?,
    @Json(name = "original_language")
    val originalLanguage: String?,
    @Json(name = "original_title")
    val originalTitle: String?,
    @Json(name = "overview")
    val overview: String,
    @Json(name = "popularity")
    val popularity: Int?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountries>,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "runtime")
    val runtime: Int,
    @Json(name = "status")
    val status: String?,
    @Json(name = "tagline")
    val tagline: String?,
    @Json(name = "title")
    val title: String,
    @Json(name = "video")
    val video: Boolean,
    @Json(name = "videos")
    val videos: List<Video>?,
    @Json(name = "cast")
    val cast: List<Cast>,
    @Json(name = "crew")
    val crew: List<Crew>,
    @Json(name = "similar")
    val similar: List<Movie>?,
    @Json(name = "recommendations")
    val recommendations: List<Movie>?,
    @Json(name = "tags")
    val tags: List<CountableTag>?,
    @Json(name = "watchlist")
    val watchlist: Boolean,
    @Json(name = "viewed")
    val viewed: Boolean,
    @Json(name = "all_reviews")
    val allReviews: List<Feed>?,
    @Json(name = "friends_reviews")
    val friendsReviews: List<Feed>?,
    @Json(name = "user_review")
    val userReview: Feed?,
    @Json(name = "availability")
    val availability: AvailabilityInfo?
) {
    fun getDirector() =
        crew.find { it.job == "Director" }

    fun getShortInfo() = "${releaseDate.subSequence(0, 3)} $runtime)"
}