package com.developer.amukovozov.nerd.ui.screens.movie_details

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { innerPadding ->
        val context = getContext()

        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let {
                    MovieDetails(context, it, innerPadding)
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

//@Composable
//private fun MovieDetails(context: Context, details: MovieDetails, innerPadding: PaddingValues) {
//    Box(Modifier.fillMaxWidth().padding(innerPadding)) {
//        val scroll = rememberScrollState(0)
//        Header(details.backdropPath, scroll.value)
//        Body(details, scroll)
//        Title(details, scroll.value)
//        Image(details.posterPath ?: "", scroll.value)
//    }
//}

@Composable
private fun Header(backdropPath: String?, scroll: Int) {
    Image(
        painter = rememberTmdbBackdropPainter(backdropPath),
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth(),
        contentDescription = null
    )

    val maxOffset = with(LocalDensity.current) { MaxHeaderOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinHeaderOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
    val futureAlpha = 1 - offset / maxOffset
    Timber.d("offset = $offset  alpha = $futureAlpha")

    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .graphicsLayer { alpha = futureAlpha }
            .background(primaryColor)
    )
}

@Composable
private fun Title(details: MovieDetails, scroll: Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
    ) {
        Spacer(Modifier.padding(8.dp))
        Text(
            text = details.title,
            style = MaterialTheme.typography.h6,
            modifier = HzPadding
        )
        details.tagline?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 20.sp,
                modifier = HzPadding
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Body(
    details: MovieDetails,
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            Surface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(TitleHeight))
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.overline,
                        color = textColor,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        text = details.overview,
                        style = MaterialTheme.typography.body1,
                        color = textColor,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = HzPadding
                    )
//                    val textButton = if (seeMore) {
//                        stringResource(id = R.string.see_more)
//                    } else {
//                        stringResource(id = R.string.see_less)
//                    }
                    Text(
                        text = details.overview,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = details.overview,
                        style = MaterialTheme.typography.overline,
                        color = textColor,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = details.overview,
                        style = MaterialTheme.typography.body1,
                        color = textColor,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))
//                    JetsnackDivider()

//                    related.forEach { snackCollection ->
//                        key(snackCollection.id) {
//                            SnackCollection(
//                                snackCollection = snackCollection,
//                                onSnackClick = { },
//                                highlight = false
//                            )
//                        }
//                    }

//                    Spacer(
//                        modifier = Modifier
//                            .padding(bottom = BottomBarHeight)
//                            .navigationBarsPadding(start = false, end = false)
//                            .height(8.dp)
//                    )
                }
            }
        }
    }
}

//@Composable
//private fun Image(
//    imageUrl: String,
//    scroll: Int
//) {
//    val collapseRange = with(LocalDensity.current) { (MaxImageOffset - MinImageOffset).toPx() }
//    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)
//
//    CollapsingImageToolbarLayout(
//        collapseFraction = collapseFraction,
//        modifier = HzPadding.then(Modifier.statusBarsPadding())
//    ) {
//        Image(
//            painter = rememberTmdbPosterPainter(imageUrl),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}


@Composable
fun CollapsingImageToolbarLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constrainsts ->
        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constrainsts.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constrainsts.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MaxImageOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constrainsts.maxWidth - imageWidth), // center when expanded
            constrainsts.maxWidth - imageWidth,
            collapseFraction
        )

        layout(
            constrainsts.maxWidth,
            imageY + imageWidth
        ) {
            imagePlaceable.place(imageX, imageY)
        }
    }
}

@Preview
@Composable
fun PreviewMovieDetails() {
    MovieDetails(
        context = getContext(),
        innerPadding = PaddingValues(),
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