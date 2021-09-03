package com.developer.amukovozov.nerd.ui.screens.browse

import com.developer.amukovozov.nerd.model.BrowseInformation
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.ScreenState

data class BrowseViewState(
    val searchField: String = "",
    val searchResult: List<Movie> = emptyList(),
    val browseInfoState: ScreenState<BrowseInformation> = Loading(),
    val isLoading: Boolean = false,
    val selectedTag: Tag? = null,
)

enum class ActiveState {
    LOADING,
    DEFAULT,
    MOVIE_SEARCH
}