package com.developer.amukovozov.nerd.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import coil.transform.CircleCropTransformation
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.Movie
import com.developer.amukovozov.nerd.model.SocialMediaLink
import com.developer.amukovozov.nerd.model.UserInfoDetails
import com.developer.amukovozov.nerd.ui.screens.feed.FeedReviewItem
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListType
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.openInChromeTab
import com.developer.amukovozov.nerd.util.ui.*
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding


object ProfileScreen {
    const val Destination = "my_profile"
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val context = getContext()
        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let {
                    ProfileInfo(
                        fullUserInfo = it,
                        onLogoutButtonClicked = viewModel::onLogoutButtonClicked,
                        onEditButtonClicked = viewModel::onEditButtonClicked,
                        onFollowersClicked = {
                            navController.navigate("profile_list/${ProfileListType.Followers.name}")
                        },
                        onFollowingsClicked = {
                            navController.navigate("profile_list/${ProfileListType.Followings.name}")
                        },
                        onLinkClicked = { link ->
                            openInChromeTab(context, link.link)
                        }
                    )
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

@Composable
fun ProfileInfo(
    fullUserInfo: FullUserInfo,
    onLogoutButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onFollowingsClicked: () -> Unit,
    onLinkClicked: (SocialMediaLink) -> Unit,
    modifier: Modifier = Modifier
) {
    val userInfo = fullUserInfo.userInfo
    ConstraintLayout(modifier.verticalScroll(rememberScrollState())) {
        val (iconLogout, iconEdit, avatar,
            nickname, followersAndFollowings,
            description, linksList, watchList,
            posts
        ) = createRefs()
        Icon(imageVector = Icons.Filled.Logout, contentDescription = null,
            modifier = Modifier.constrainAs(iconLogout) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })
        Icon(imageVector = Icons.Filled.Edit, contentDescription = null,
            modifier = Modifier.constrainAs(iconEdit) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(iconLogout.start, margin = 16.dp)
            }
        )
        Image(
            painter = rememberCoilPainter(
                request = userInfo.avatarPath,
                requestBuilder = {
                    transformations(CircleCropTransformation())
                }),
            modifier = Modifier
                .height(64.dp)
                .width(64.dp)
                .constrainAs(avatar) {
                    top.linkTo(iconEdit.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                },
            contentDescription = null
        )

        Text(
            text = userInfo.nickname,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.constrainAs(nickname) {
                width = Dimension.fillToConstraints
                top.linkTo(avatar.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            }
        )
        ProfileFollowingsAndFollowersView(
            fullUserInfo,
            onFollowersClicked,
            onFollowingsClicked,
            Modifier.constrainAs(followersAndFollowings) {
                top.linkTo(nickname.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            })

        userInfo.description?.let {
            Text(text = it,
                modifier = Modifier.constrainAs(description) {
                    width = Dimension.fillToConstraints
                    top.linkTo(followersAndFollowings.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                })
        }
        userInfo.links?.let { links ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(linksList) {
                    width = Dimension.fillToConstraints
                    top.linkTo(description.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                }) {
                links.forEach { link ->
                    ProfileSocialMediaLink(link = link) {
                        onLinkClicked.invoke(it)
                    }
                }
            }
        }

        ProfileCollection(
            modifier = Modifier.constrainAs(watchList) {
                top.linkTo(linksList.bottom, margin = 16.dp)
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            },
            collectionTitle = stringResource(R.string.watchlist_title),
            items = {
                items(fullUserInfo.watchList) {
                    WatchlistItem(it)
                }
            })
        ProfileCollection(
            modifier = Modifier.constrainAs(posts) {
                top.linkTo(watchList.bottom, margin = 16.dp)
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            },
            collectionTitle = stringResource(R.string.user_posts_title)
        ) {
            items(fullUserInfo.posts) {
                WatchlistItem(movie = it.movie)
            }
        }
    }
}

@Composable
fun ProfileCollection(
    modifier: Modifier,
    collectionTitle: String,
    items: LazyListScope.() -> Unit
) {
    Column(modifier = modifier) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                text = collectionTitle,
                modifier = Modifier.align(Alignment.TopStart),
                style = MaterialTheme.typography.subtitle1,
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
fun WatchlistItem(movie: Movie) {
    Column {
        Image(
            painter = rememberTmdbPosterPainter(movie.posterPath),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .width(88.dp)
                .height(130.dp),
            contentDescription = null
        )
    }
}

@Composable
fun ProfileFollowingsAndFollowersView(
    fullUserInfo: FullUserInfo,
    onFollowersClicked: () -> Unit,
    onFollowingsClicked: () -> Unit,
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
                    onFollowersClicked.invoke()
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
                    onFollowingsClicked.invoke()
                }
        )
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
@Preview
fun ProfilePreview() {
    ProfileInfo(
        FullUserInfo(
            UserInfoDetails("1", "muk@ku.ru", "kitaec", "description", null, null),
            5,
            56,
            emptyList(),
            emptyList()
        ),
        {},
        {},
        {},
        {},
        {}
    )
}