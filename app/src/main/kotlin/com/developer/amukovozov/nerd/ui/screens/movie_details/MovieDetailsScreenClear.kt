package com.developer.amukovozov.nerd.ui.screens.movie_details

import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.developer.amukovozov.nerd.model.feed.CountableTag
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Cast
import com.developer.amukovozov.nerd.model.movie.Genres
import com.developer.amukovozov.nerd.model.movie.MovieDetails
import com.developer.amukovozov.nerd.ui.components.ChipWithCounter
import com.developer.amukovozov.nerd.ui.screens.feed.FeedList
import com.developer.amukovozov.nerd.ui.screens.feed.FeedReviewItem
import com.developer.amukovozov.nerd.ui.theme.backgroundColor
import com.developer.amukovozov.nerd.ui.theme.primaryTextColor
import com.developer.amukovozov.nerd.ui.theme.secondaryTextColor
import com.developer.amukovozov.nerd.ui.theme.white
import com.developer.amukovozov.nerd.util.ui.getContext
import com.developer.amukovozov.nerd.util.ui.rememberTmdbBackdropPainter
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter
import com.google.accompanist.insets.navigationBarsPadding

private val MinHeaderOffset = 0.dp
private val MaxHeaderOffset = 200.dp

private val MinTitleOffset = 0.dp
private val MaxTitleOffset = MinTitleOffset + MaxHeaderOffset
private val TitleMinHeight = 64.dp

private val BottomBarHeight = 56.dp

private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun MovieDetails(context: Context, details: MovieDetails, innerPadding: PaddingValues) {
    val scrollState = rememberScrollState(0)
    Box(
        Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .then(HzPadding)
                .verticalScroll(scrollState)
        ) {
            val (backDrop, title, tagline, poster, overview,
                shortInfo, director, seeMoreButton,
                tags, friendsFeedTitle, friendsFeed) = createRefs()

            Box(modifier = Modifier.constrainAs(backDrop) {
                top.linkTo((parent.top))
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Image(
                    painter = rememberTmdbBackdropPainter(details.backdropPath),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaxHeaderOffset),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
                val gradientStartY = with(LocalDensity.current) { MaxTitleOffset.toPx() } / 2
                Spacer(
                    Modifier
                        .height(MaxHeaderOffset)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, backgroundColor),
                                gradientStartY
                            ),
                        )
                )
            }
            Text(
                text = details.title,
                style = MaterialTheme.typography.h6,
                color = primaryTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .constrainAs(title) {
                        top.linkTo(backDrop.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
            Text(
                text = details.getShortInfo(),
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                color = secondaryTextColor,
                maxLines = 1,
                modifier = Modifier
                    .constrainAs(shortInfo) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
            details.getDirector()?.let {
                Text(
                    text = "Режиссер:\n ${it.name}",
                    color = primaryTextColor,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.constrainAs(director) {
                        top.linkTo(shortInfo.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(poster.start, margin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
                )
            }
            Image(
                painter = rememberTmdbPosterPainter(details.posterPath),
                modifier = Modifier.constrainAs(poster) {
                    top.linkTo(shortInfo.bottom, margin = 4.dp)
                    end.linkTo(parent.end)
                    height = Dimension.value(150.dp)
                },
                contentDescription = null
            )

            var isSeeMoreButtonVisible by remember { mutableStateOf(true) }
            Text(
                text = details.overview,
                color = primaryTextColor,
                maxLines = if (isSeeMoreButtonVisible) 5 else Int.MAX_VALUE,
                style = MaterialTheme.typography.body1,
                overflow = if (isSeeMoreButtonVisible) TextOverflow.Ellipsis else TextOverflow.Visible,
                modifier = Modifier
                    .constrainAs(overview) {
                        top.linkTo(poster.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .animateContentSize()
            )
            val imageVector = if (isSeeMoreButtonVisible) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp
            var image by remember { mutableStateOf(imageVector) }
            image = imageVector
            Crossfade(
                targetState = image,
                modifier = Modifier.constrainAs(seeMoreButton) {
                    top.linkTo(overview.bottom)
                    start.linkTo(overview.start)
                    end.linkTo(overview.end)
                }) {
                Icon(
                    it,
                    tint = white,
                    modifier = Modifier
                        .size(42.dp)
                        .padding(4.dp)
                        .clickable { isSeeMoreButtonVisible = !isSeeMoreButtonVisible },
                    contentDescription = null
                )
            }
            details.tags?.let {
                LazyRow(modifier = Modifier.constrainAs(tags) {
                    top.linkTo(seeMoreButton.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }) {
                    items(it) {
                        ChipWithCounter(
                            it.tag.title, it.tag.emojiCode, it.tag.backgroundColor, it.tag.textColor, it.count,
                            Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
                        )
                    }
                }
            }

            if (!details.friendsReviews.isNullOrEmpty()) {
                Text(
                    text = "Ревью друзей",
                    style = MaterialTheme.typography.subtitle2,
                    color = primaryTextColor,
                    modifier = Modifier.constrainAs(friendsFeedTitle) {
                        top.linkTo(tags.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                    }
                )
                details.friendsReviews.map {
                    FeedReviewItem(feed = it, onLikeClicked = { _, _ -> }, onReviewClicked = {}) {
                    }
                }
            }
        }
    }

    @Composable
    fun ActivitiesBottomBar(details: MovieDetails) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .navigationBarsPadding()
                .then(HzPadding)
                .heightIn(BottomBarHeight)
        ) {

        }
    }

    @Preview
    @Composable
    fun PreviewMovieDetailsClear() {
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
                listOf(
                    CountableTag(Tag(1, "perviy", null, "33C22D3D", "FFC22D3D"), count = 1),
                    CountableTag(Tag(2, "vtoroy", null, "3373BFEE", "FF73BFEE"), count = 12),
                    CountableTag(Tag(3, "tretiy", null, "33EECAA4", "FFEECAA4"), count = 3)
                ),
                false,
                false,
                listOf(),
                listOf(),
                null,
                null
            )
        )
    }
}
