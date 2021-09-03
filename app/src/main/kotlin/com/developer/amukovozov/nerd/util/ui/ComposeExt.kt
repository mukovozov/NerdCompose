package com.developer.amukovozov.nerd.util.ui

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.developer.amukovozov.nerd.R
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.LoadPainter

private const val TMDB_BASE_URL = "https://image.tmdb.org/t/p"
private const val POSTER_WIDTH = "w500"
private const val PROFILE_WIDTH = "w100"
private const val BACKDROP_PATH = "w500"

@Composable
fun rememberTmdbPosterPainter(
    posterPath: String?,
    @DrawableRes previewPlaceHolder: Int = R.drawable.ic_launcher_foreground,
    requestBuilder: (ImageRequest.Builder.(size: IntSize) -> ImageRequest.Builder)? = null
): LoadPainter<Any> {
    return rememberTmdbCoilPainter(POSTER_WIDTH, posterPath, previewPlaceHolder, requestBuilder)
}

@Composable
fun rememberProfilePainter(
    avatarPath: String?,
    @DrawableRes previewPlaceHolder: Int = R.drawable.ic_launcher_foreground,
    requestBuilder: (ImageRequest.Builder.(size: IntSize) -> ImageRequest.Builder)? = null
): LoadPainter<Any> {
    return rememberCoilPainter(
        request = avatarPath,
        requestBuilder = { transformations(CircleCropTransformation()) },
        fadeIn = true,
        previewPlaceholder = R.drawable.ic_user_placeholder
    )

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
    @DrawableRes previewPlaceHolder: Int,
    requestBuilder: (ImageRequest.Builder.(size: IntSize) -> ImageRequest.Builder)? = null,
): LoadPainter<Any> {
    return rememberCoilPainter(
        request = "$TMDB_BASE_URL/$width/$imagePath",
        requestBuilder = requestBuilder,
        fadeIn = true,
        previewPlaceholder = previewPlaceHolder
    )
}

@Composable
fun getContext() = LocalContext.current