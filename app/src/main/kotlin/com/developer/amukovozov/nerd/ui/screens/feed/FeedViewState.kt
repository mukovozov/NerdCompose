package com.developer.amukovozov.nerd.ui.screens.feed

import com.developer.amukovozov.nerd.model.feed.Feed
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.ScreenState

data class FeedViewState(
    val screenState: ScreenState<List<Feed>> = Loading()
)