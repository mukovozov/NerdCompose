package com.developer.amukovozov.nerd.ui.screens.movie_details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.developer.amukovozov.nerd.model.movie.Cast
import com.developer.amukovozov.nerd.model.movie.Genres
import com.developer.amukovozov.nerd.model.movie.MovieDetails
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.ui.theme.textColor
import com.developer.amukovozov.nerd.ui.theme.white
import com.developer.amukovozov.nerd.util.ui.*
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min

private val TitleHeight = 120.dp
private val GradientScroll = 180.dp
private val MaxImageOffset = 250.dp
private val MinImageOffset = 12.dp
private val MinHeaderOffset = 0.dp
private val MaxHeaderOffset = 280.dp
private val MinTitleOffset = 40.dp
private val MaxTitleOffset = MinTitleOffset + GradientScroll
private val ExpandedImageSize = 150.dp
private val CollapsedImageSize = 150.dp

private val HzPadding = Modifier.padding(horizontal = 24.dp)

object MovieDetailsScreen {
    private const val Route = "movie_details"
    const val Argument = "id"
    const val Destination = "$Route/{$Argument}"

    fun createDestination(movieId: Int) = "$Route/$movieId"
}


@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel,
    modifier: Modifier = Modifier
) {
    viewModel.onScreenOpened(movieId)

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = RoundedCornerShape(50),
                backgroundColor = primaryColor
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            val screenState = viewModel.viewState.screenState
            if (screenState is Content) {
                BottomAppBar(
                    cutoutShape = RoundedCornerShape(50),
                    content = {
                        val isInWatchlist = screenState.content?.watchlist ?: false
                        ActivitiesBottomBar(movieId, isInWatchlist, viewModel::onShareButtonClicked, viewModel::onWatchlistButtonClicked)
                    }
                )
            }
        }
    ) {
        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let {
                    MovieDetails(it)
                }
            }
            is Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is Stub -> {
                // todo show stub
            }
        }
    }
}

@Preview
@Composable
fun PreviewMovieDetails() {
    MovieDetails(
        details = MovieDetails(
            515087,
            "/flPdWe3qZPZfPUGIr2PKodeqIwz.jpg",
            null,
            listOf(Genres(18, "драма")),
            "tt6458566",
            "en",
            "Esau",
            "Эсав, по одноименному роману израильского автора Меира Шалева, следует за 40-летним писателем, который спустя полжизни возвращается в дом своей семьи, чтобы встретиться с братом, укравшим его любовь и средства к существованию. Эта история — современная интерпретация библейской истории Иакова и Исава из «Книги Бытия».",
            0,
            "/1hr94FDYyXzqGpfU3leAFbUkxm0.jpg",
            listOf(),
            "2019-11-04",
            114,
            "Released",
            "tagline",
            "Эсав",
            false,
            emptyList(),
            listOf(
                Cast(
                    false,
                    2,
                    164,
                    "Acting",
                    "Lior Ashkenazi",
                    "Lior Ashkenazi",
                    1.62,
                    "/c2IJJuYiqZNHoZVikp6HgCgbGkK.jpg",
                    12,
                    "Esau",
                    "5abdeb890e0a264a5d008a54",
                    0
                )
            ),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            false,
            false,
            listOf(),
            listOf(),
            null,
            null
        )
    )
}