package com.developer.amukovozov.nerd.util.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.developer.amukovozov.nerd.R

private const val TMDB_BASE_URL = "https://image.tmdb.org/t/p"
private const val POSTER_WIDTH = "w500"
private const val PLATFORM_ICON_WIDTH = "w200"
private const val BACKDROP_PATH = "w500"

@Composable
fun rememberAvailablePlatformPainter(
    platformIconPath: String?,
): ImagePainter {
    return rememberTmdbCoilPainter(
        PLATFORM_ICON_WIDTH,
        platformIconPath,
        R.drawable.ic_user_placeholder,
        requestBuilder = { transformations(CircleCropTransformation()) }
    )
}

@Composable
fun rememberTmdbPosterPainter(
    posterPath: String?,
    @DrawableRes previewPlaceHolder: Int = R.drawable.ic_launcher_foreground,
    requestBuilder: (ImageRequest.Builder.() -> Unit) = {}
): ImagePainter {
    return rememberTmdbCoilPainter(POSTER_WIDTH, posterPath, previewPlaceHolder, requestBuilder)
}

@Composable
fun rememberProfilePainter(
    avatarPath: String?
): ImagePainter {
    return rememberImagePainter(
        data = avatarPath,
        builder = {
            crossfade(true)
            placeholder(R.drawable.ic_user_placeholder)
            transformations(CircleCropTransformation())
        }
    )

}

@Composable
fun rememberTmdbBackdropPainter(
    backdropPath: String?,
    @DrawableRes previewPlaceHolder: Int = R.drawable.ic_launcher_foreground
): ImagePainter {
    return rememberTmdbCoilPainter(BACKDROP_PATH, backdropPath, previewPlaceHolder)
}

@Composable
private fun rememberTmdbCoilPainter(
    width: String,
    imagePath: String?,
    //todo change placeholder
    @DrawableRes previewPlaceHolder: Int,
    requestBuilder: (ImageRequest.Builder.() -> Unit) = {
        crossfade(true)
        placeholder(previewPlaceHolder)
    },
): ImagePainter {
    return rememberImagePainter(
        data = "$TMDB_BASE_URL/$width/$imagePath",
        builder = requestBuilder
    )
}

@Composable
fun getContext() = LocalContext.current