package com.developer.amukovozov.nerd.network.adapter

import com.developer.amukovozov.nerd.model.LinkType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class SocialMediaLinkTypeAdapter {
    @ToJson
    fun toJson(linkType: LinkType): String {
        return when (linkType) {
            LinkType.Twitter -> "twitter"
            LinkType.Inst -> "inst"
        }
    }

    @FromJson
    fun fromJson(string: String): LinkType {
        return when (string) {
            "twitter" -> LinkType.Twitter
            "inst" -> LinkType.Inst
            else -> LinkType.Default
        }
    }
}