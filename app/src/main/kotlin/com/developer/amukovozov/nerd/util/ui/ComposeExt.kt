package com.developer.amukovozov.nerd.util.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.developer.amukovozov.nerd.R
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainter

private const val TMDB_BASE_URL = "https://image.tmdb.org/t/p"
private const val POSTER_WIDTH = "w500"
private const val BACKDROP_PATH = "w1280"

@Composable
fun rememberTmdbPosterPainter(
    posterPath: String?,
    @DrawableRes previewPlaceHolder: Int = R.drawable.ic_launcher_foreground
): LoadPainter<Any> {
    return rememberTmdbCoilPainter(POSTER_WIDTH, posterPath, previewPlaceHolder)
}

@Composable
fun rememberTmdbBackdropPainter(
    backdropPath: String?,
    @DrawableRes previewPlaceHolder: Int = R.drawable.ic_launcher_foreground
): LoadPainter<Any> {
    return rememberTmdbCoilPainter(BACKDROP_PATH, backdropPath, previewPlaceHolder)
}

@Composable
private fun rememberTmdbCoilPainter(
    width: String,
    imagePath: String?,
    //todo change placeholder
    @DrawableRes previewPlaceHolder: Int
): LoadPainter<Any> {
    return rememberCoilPainter(
        request = "$TMDB_BASE_URL/$width/$imagePath",
        fadeIn = true,
        previewPlaceholder = previewPlaceHolder
    )
}