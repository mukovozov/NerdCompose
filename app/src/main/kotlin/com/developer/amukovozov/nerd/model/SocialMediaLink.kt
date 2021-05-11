package com.developer.amukovozov.nerd.model

import androidx.annotation.DrawableRes
import com.developer.amukovozov.nerd.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SocialMediaLink(
    @Json(name = "link_type")
    val linkType: LinkType,
    @Json(name = "link_postfix")
    val linkPostfix: String,
    @Json(name = "link")
    val link: String
)

enum class LinkType(@DrawableRes val icon: Int) {
    Twitter(R.drawable.ic_twitter_logo),
    Inst(R.drawable.ic_instagram_logo);

    companion object {
        val Default = Inst
    }
}