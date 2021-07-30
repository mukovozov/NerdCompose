package com.developer.amukovozov.nerd.ui.screens.movie_details

import com.developer.amukovozov.nerd.model.movie.MovieDetails
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.ScreenState

data class MovieDetailsViewState(
    val screenState: ScreenState<MovieDetails> = Loading()
)