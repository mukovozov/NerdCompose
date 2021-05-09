package com.developer.amukovozov.nerd.model

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

enum class LinkType {
    Twitter,
    Inst;

    companion object {
        val Default = Inst
    }
}