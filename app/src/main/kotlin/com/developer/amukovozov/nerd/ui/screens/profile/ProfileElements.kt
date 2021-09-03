package com.developer.amukovozov.nerd.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.transform.CircleCropTransformation
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.model.SocialMediaLink
import com.developer.amukovozov.nerd.model.UserInfoDetails
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun UserPostsTitle(fullUserInfo: FullUserInfo) {
    if (fullUserInfo.posts.isEmpty()) {
        Text(
            text = stringResource(R.string.user_posts_stub_title),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
    } else {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.user_posts_title),
                    modifier = Modifier.align(Alignment.TopStart),
                    style = MaterialTheme.typography.h6
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    modifier = Modifier.align(Alignment.TopEnd),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun UserWatchlist(fullUserInfo: FullUserInfo, onMovieClicked: (movieId: Int) -> Unit) {
    if (fullUserInfo.watchList.isEmpty()) {
        Text(
            text = stringResource(R.string.watchlist_stub_title),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
    } else {
        WatchlistCollection(
            collectionTitle = stringResource(R.string.watchlist_title),
            items = {
                items(fullUserInfo.watchList) {
                    WatchlistItem(it, onMovieClicked)
                }
            }
        )
    }
}

@Composable
fun UserSocialMediaLinks(
    userInfo: UserInfoDetails,
    onLinkClicked: (SocialMediaLink) -> Unit
) {
    userInfo.links?.let { links -> SocialMediaLinks(links, onLinkClicked) }
}

@Composable
fun UserDescription(userInfo: UserInfoDetails) {
    userInfo.description?.let {
        Text(
            text = it, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )
    }
}

@Composable
fun MainProfileInfo(userInfo: UserInfoDetails) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberCoilPainter(
                request = userInfo.avatarPath,
                requestBuilder = {
                    transformations(CircleCropTransformation())
                }),
            modifier = Modifier
                .height(64.dp)
                .width(64.dp),
            contentDescription = null
        )
        Text(
            text = userInfo.nickname,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(top = 8.dp, start = 24.dp)
        )
    }
}

@Composable
fun ProfileFollowingsAndFollowersView(
    fullUserInfo: FullUserInfo,
    onFollowersClicked: (userId: Int) -> Unit,
    onFollowingsClicked: (userId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.followers_with_count_title, fullUserInfo.followers),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .border(width = 2.dp, primaryColor, RoundedCornerShape(12.dp))
                .padding(8.dp)
                .clickable {
                    onFollowersClicked.invoke(fullUserInfo.userInfo.id.toInt())
                }
        )
        Text(
            text = stringResource(R.string.followings_with_count_title, fullUserInfo.followings),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .border(width = 2.dp, primaryColor, RoundedCornerShape(12.dp))
                .padding(8.dp)
                .clickable {
                    onFollowingsClicked.invoke(fullUserInfo.userInfo.id.toInt())
                }
        )
    }
}

@Composable
private fun SocialMediaLinks(
    links: List<SocialMediaLink>,
    onLinkClicked: (SocialMediaLink) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        links.forEach { link ->
            ProfileSocialMediaLink(link = link) {
                onLinkClicked.invoke(it)
            }
        }
    }
}

@Composable
private fun ProfileSocialMediaLink(link: SocialMediaLink, onLinkClicked: (SocialMediaLink) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onLinkClicked.invoke(link)
        }) {
        Icon(
            painter = painterResource(link.linkType.icon),
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            contentDescription = null
        )
        Text(
            text = "@${link.linkPostfix}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp)
        )
    }
}

@Composable
fun WatchlistCollection(
    modifier: Modifier = Modifier,
    collectionTitle: String,
    items: LazyListScope.() -> Unit,
    collectionTitleStyle: TextStyle = MaterialTheme.typography.h6
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                text = collectionTitle,
                modifier = Modifier.align(Alignment.TopStart),
                style = collectionTitleStyle
            )
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                modifier = Modifier.align(Alignment.TopEnd),
                contentDescription = null
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items.invoke(this)
        }
    }
}

@Composable
fun WatchlistItem(movie: Movie, onMovieClicked: (movieId: Int) -> Unit) {
    Column {
        Image(
            painter = rememberTmdbPosterPainter(movie.posterPath),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .width(88.dp)
                .height(130.dp)
                .clickable { onMovieClicked.invoke(movie.id) }
            ,
            contentDescription = null
        )
    }
}
