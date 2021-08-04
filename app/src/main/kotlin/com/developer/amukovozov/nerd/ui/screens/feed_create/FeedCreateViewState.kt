package com.developer.amukovozov.nerd.ui.screens.feed_create

import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.ui.components.searchbar.autocomplete.AutoCompleteState

private const val TOTAL_CREATION_INDEX = 3

data class FeedCreateViewState(
    val movieId: Int? = null,

    val movie: Movie? = null,
    val review: ReviewFieldState = ReviewFieldState(),
    val tags: List<TagState> = emptyList(),

    val progressIndex: Int = 0,
    val totalStepsIndex: Int = TOTAL_CREATION_INDEX,

    val autoCompleteState: AutoCompleteState<Movie> = AutoCompleteState(emptyList()),

    val isNextButtonEnabled: Boolean = false,

    // придумать как вынести в event и кидать из viewModel
    val isReviewPublished: Boolean = false
)

data class TagState(val tag: Tag, val isSelected: Boolean)

data class ReviewFieldState(val value: String = "") : TextFieldState(value, ::isReviewValid, ::reviewValidationError)

private fun reviewValidationError(review: String): String {
    return "Минимум 4 символа"
}

private fun isReviewValid(review: String): Boolean {
    return review.length > 4
}