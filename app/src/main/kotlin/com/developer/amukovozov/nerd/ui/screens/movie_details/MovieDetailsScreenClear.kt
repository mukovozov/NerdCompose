package com.developer.amukovozov.nerd.ui.screens.movie_details

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.feed.CountableTag
import com.developer.amukovozov.nerd.model.feed.Feed
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.*
import com.developer.amukovozov.nerd.ui.components.CountableTagsGroup
import com.developer.amukovozov.nerd.ui.screens.feed.ShortFeedReviewItem
import com.developer.amukovozov.nerd.ui.theme.*
import com.developer.amukovozov.nerd.util.ui.getContext
import com.developer.amukovozov.nerd.util.ui.rememberTmdbBackdropPainter
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter

private val MinHeaderOffset = 0.dp
private val MaxHeaderOffset = 200.dp

private val MinTitleOffset = 0.dp
private val MaxTitleOffset = MinTitleOffset + MaxHeaderOffset
private val TitleMinHeight = 64.dp

private val BottomBarHeight = 56.dp

private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun MovieDetails(details: MovieDetails) {
    val scrollState = rememberScrollState(0)
    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 64.dp)
    ) {
        LazyColumn() {
            item { Backdrop(details.backdropPath) }
            item { HeaderMovieInfo(details) }
            item { Overview(details.overview) }
            details.tags?.let { item { Tags(it) } }
            if (!details.friendsReviews.isNullOrEmpty()) {
                item { FriendsReviews(details.friendsReviews) }
            }
        }
    }
}

@Composable
fun MemberItem(member: Member) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp)
            .heightIn(max = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberTmdbPosterPainter(member.profilePath), contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Text(
            member.name,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.caption,
            color = primaryTextColor,
            textAlign = TextAlign.Center
        )
        Text(
            member.role,
            modifier = Modifier.padding(top = 2.dp),
            style = MaterialTheme.typography.caption,
            color = secondaryTextColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Backdrop(backdropPath: String?) {
    Box {
        Image(
            painter = rememberTmdbBackdropPainter(backdropPath),
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
}

@Composable
private fun HeaderMovieInfo(details: MovieDetails) {
    Row() {
        Column(modifier = Modifier.weight(2f)) {
            Text(
                text = details.title,
                style = MaterialTheme.typography.h6,
                color = primaryTextColor,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )

            details.getDirector()?.let {
                Text(
                    text = "Режиссер\n${it.name}",
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
            ) {
                Text(
                    text = details.releaseYear,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Center,
                    color = secondaryTextColor,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(end = 2.dp)
                )
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .background(primaryDarkColor, CircleShape)
                )
                Text(
                    text = stringResource(R.string.movie_duration_text, details.runtime),
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Center,
                    color = secondaryTextColor,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 2.dp)
                )
            }

            details.availability?.let {
                Text(
                    text = "Где посмотреть ->",
                    color = secondaryTextColor,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .clickable { }
                )
            }
        }

        Image(
            painter = rememberTmdbPosterPainter(details.posterPath),
            modifier = Modifier
                .weight(1f)
                .padding(top = 2.dp, end = 16.dp)
                .height(150.dp),
            contentDescription = null
        )
    }
}

@Composable
private fun Overview(overview: String) {
    var isSeeMoreButtonVisible by remember { mutableStateOf(true) }
    val buttonAlpha = if (isSeeMoreButtonVisible) 1f else 0f
    val buttonAlphaState: Float by animateFloatAsState(buttonAlpha)
    Column {
        Text(
            text = overview,
            color = primaryTextColor,
            maxLines = if (isSeeMoreButtonVisible) 5 else Int.MAX_VALUE,
            style = MaterialTheme.typography.body1,
            overflow = if (isSeeMoreButtonVisible) TextOverflow.Ellipsis else TextOverflow.Visible,
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clickable { isSeeMoreButtonVisible = !isSeeMoreButtonVisible }
                .animateContentSize()
        )
        Icon(
            imageVector = Icons.Default.MoreHoriz,
            tint = secondaryTextColor,
            modifier = Modifier
                .padding(top = 2.dp)
                .align(Alignment.CenterHorizontally)
                .graphicsLayer { alpha = buttonAlphaState },
            contentDescription = null
        )
    }
}

@Composable
private fun Tags(tags: List<CountableTag>) {
    CountableTagsGroup(
        tags, modifier = Modifier
            .wrapContentHeight()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun FriendsReviews(reviews: List<Feed>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {

        Text(
            text = "Ревью друзей",
            style = MaterialTheme.typography.h6,
            color = primaryTextColor
        )
        LazyRow(
            modifier = Modifier
                .padding(top = 8.dp)
                .heightIn(max = 200.dp)
        ) {
            items(reviews) { feed ->
                ShortFeedReviewItem(feed = feed, modifier = Modifier.fillParentMaxWidth())
            }
        }
    }
}

@Composable
fun ActivitiesBottomBar(
    movieId: Int,
    isInWatchlist: Boolean,
    onShareButtonClicked: () -> Unit,
    onWatchlistButtonClicked: (movieId: Int, isInWatchlist: Boolean) -> Unit
) {
    Surface(
        color = backgroundColor,
        elevation = 8.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(BottomBarHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .padding(start = 16.dp)
                    .clickable {
                        onWatchlistButtonClicked.invoke(movieId, !isInWatchlist)
                    }) {
                Icon(
                    if (isInWatchlist) Icons.Default.BookmarkBorder else {
                        Icons.Default.Bookmark
                    },
                    tint = white,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .padding(start = 16.dp)
                    .clickable { onShareButtonClicked.invoke() }) {
                Icon(
                    Icons.Default.Share,
                    tint = white,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
        }
    }

}

@Preview
@Composable
fun HeaderMovieInfoPreview() {
    HeaderMovieInfo(details)
}

@Preview
@Composable
fun TagsPreview() {
    ActivitiesBottomBar(details.id, details.watchlist, {}, {_, _ ->})
}

@Preview
@Composable
fun PreviewMovieDetailsClear() {
    MovieDetails(
        details = details
    )
}


private val details = MovieDetails(
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
    listOf(
        Crew(
            false,
            2,
            164,
            "Acting",
            "Lior Ashkenazi",
            "Lior Ashkenazi",
            1.62,
            "/c2IJJuYiqZNHoZVikp6HgCgbGkK.jpg",
            "12",
            "Director",
            "Director",
            ""
        )
    ),
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
    AvailabilityInfo(
        1, "", 1, ""
    )
)
