package com.developer.amukovozov.nerd.network.adapter

import com.developer.amukovozov.nerd.model.feed.FeedType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class FeedTypeAdapter {

    @ToJson
    fun toJson(reviewType: FeedType): String {
        return when (reviewType) {
            FeedType.Review -> "review"
        }
    }

    @FromJson
    fun fromJson(string: String): FeedType {
        return when (string) {
            "review" -> FeedType.Review
            else -> FeedType.Default
        }
    }
}