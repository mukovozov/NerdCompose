package com.developer.amukovozov.nerd.model

import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie

data class BrowseInformation(
    val popularMovies: List<Movie>,
    val tags: List<Tag>,
    val users: List<UserInfo>
)