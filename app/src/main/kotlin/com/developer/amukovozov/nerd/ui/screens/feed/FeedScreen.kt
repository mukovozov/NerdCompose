package com.developer.amukovozov.nerd.ui.screens.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.model.*
import com.developer.amukovozov.nerd.ui.components.TagsGroup
import com.developer.amukovozov.nerd.ui.theme.backgroundAccentColor
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.ui.Content
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.Stub
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter
import java.util.*

@Composable
fun Feed(
    viewModel: FeedViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) {
        when (val screenState = viewModel.viewState.screenState) {
            is Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is Content -> {
                screenState.content?.let {
                    FeedList(
                        feeds = it,
                        onLikeClicked = viewModel::onLikeClicked
                    )
                }
            }
            is Stub -> {

            }

        }
    }
}

@Composable
fun FeedList(
    feeds: List<Feed>,
    onLikeClicked: (FeedId: Int, isLiked: Boolean) -> Unit
) {
    LazyColumn {
        items(feeds) { feed ->
            FeedReviewItem(feed = feed,
                onLikeClicked = onLikeClicked,
                onReviewClicked = {},
                onUserClicked = {}
            )
        }
    }
}

@Composable
private fun FeedReviewItem(
    feed: Feed,
    onLikeClicked: (feedId: Int, isLiked: Boolean) -> Unit,
    onReviewClicked: () -> Unit,
    onUserClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = backgroundAccentColor,
        elevation = 8.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = feed.userInfo.nickname,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .clickable { onUserClicked.invoke() }
                    .padding(4.dp)
            )
            UserReviewBlock(feed, onReviewClicked)
            FeedLikeView(
                feedId = feed.id,
                isUserLiked = feed.isLiked,
                likesCount = feed.likesCount,
                onLikeClicked = onLikeClicked,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun UserReviewBlock(feed: Feed, onReviewClicked: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onReviewClicked.invoke() }) {
        Image(
            painter = rememberTmdbPosterPainter(feed.movie.posterPath),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .width(88.dp)
                .height(130.dp)
        )

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = feed.movie.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth()
            )
            feed.tags?.let {
                TagsGroup(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp), tags = it)
            }
            Text(
                text = feed.userReview,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun FeedLikeView(
    feedId: Int,
    isUserLiked: Boolean,
    likesCount: Int,
    onLikeClicked: (FeedId: Int, isLiked: Boolean) -> Unit,
    modifier: Modifier
) {
    val imageVector = if (isUserLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    var image by remember { mutableStateOf(imageVector) }
    image = imageVector

    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false)
        ) {
            onLikeClicked.invoke(feedId, !isUserLiked)
        }, verticalAlignment = Alignment.CenterVertically
    ) {
        Crossfade(targetState = image) {
            Icon(
                imageVector = it,
                tint = primaryColor,
                modifier = Modifier.padding(4.dp),
                contentDescription = null
            )
        }
        if (likesCount > 0) {
            Text(text = likesCount.toString(), modifier = Modifier.padding(start = 4.dp, end = 4.dp))
        }
    }
}

@Composable
@Preview
fun FeedReviewItemPreview() {
    FeedReviewItem(
        feed = Feed(
            0, false, 2, FeedType.Review, Date().time, "233423sdfnksdf",
            Movie(438650, "", "Снегоуборщик"),
            listOf(Tag(1, "test1", null, null, null), Tag(2, "test2", null, null, null)),
            UserInfo(1, "mail@info.com", "nickname")
        ),
        onReviewClicked = {},
        onLikeClicked = { id, isLiked -> },
        onUserClicked = {}
    )
}