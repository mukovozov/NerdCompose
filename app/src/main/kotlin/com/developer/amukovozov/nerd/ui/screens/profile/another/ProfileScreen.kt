package com.developer.amukovozov.nerd.ui.screens.profile.another

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.SocialMediaLink
import com.developer.amukovozov.nerd.ui.screens.feed.FeedReviewItem
import com.developer.amukovozov.nerd.ui.screens.movie_details.MovieDetailsScreen
import com.developer.amukovozov.nerd.ui.screens.profile.*
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListScreen
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListType
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.openInChromeTab
import com.developer.amukovozov.nerd.util.ui.Content
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.Stub
import com.developer.amukovozov.nerd.util.ui.getContext
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

object ProfileScreen {
    private const val Route = "profile"
    const val Argument = "userId"
    const val Destination = "$Route/{$Argument}"

    fun createDestination(userId: Int) = "$Route/$userId"
}

@Composable
fun ProfileScreen(
    userId: Int,
    viewModel: ProfileViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    viewModel.onScreenOpened(userId)
    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val context = getContext()
        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let { userInfo ->
                    ProfileInfo(
                        fullUserInfo = userInfo,
                        onFollowersClicked = { userId ->
                            navController.navigate(
                                ProfileListScreen.createDestination(ProfileListType.Followers, userId)
                            )
                        },
                        onFollowingsClicked = { userId ->
                            navController.navigate(
                                ProfileListScreen.createDestination(ProfileListType.Followings, userId)
                            )
                        },
                        onLinkClicked = { link ->
                            openInChromeTab(context, link.link)
                        },
                        onFollowButtonClicked = {
                            viewModel.onFollowButtonClicked(it, userId)
                        },
                        onWatchlistMovieClicked = { movieId ->
                            navController.navigate(
                                MovieDetailsScreen.createDestination(movieId)
                            )
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
private fun ProfileInfo(
    fullUserInfo: FullUserInfo,
    onFollowersClicked: (userId: Int) -> Unit,
    onFollowingsClicked: (userId: Int) -> Unit,
    onLinkClicked: (SocialMediaLink) -> Unit,
    onFollowButtonClicked: (isFollowedByYou: Boolean) -> Unit,
    onWatchlistMovieClicked: (movieId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(top = 16.dp)) {
        item {
            ProfileInformation(
                fullUserInfo = fullUserInfo,
                onFollowersClicked = onFollowersClicked,
                onFollowingsClicked = onFollowingsClicked,
                onLinkClicked = onLinkClicked,
                onFollowButtonClicked = onFollowButtonClicked,
                onWatchlistMovieClicked = onWatchlistMovieClicked
            )
        }
        if (fullUserInfo.posts.isNotEmpty()) {
            items(fullUserInfo.posts) {
                FeedReviewItem(feed = it, { _, _ -> }, {}, {})
            }
        }
    }
}

@Composable
private fun ProfileInformation(
    fullUserInfo: FullUserInfo,
    onFollowersClicked: (userId: Int) -> Unit,
    onFollowingsClicked: (userId: Int) -> Unit,
    onLinkClicked: (SocialMediaLink) -> Unit,
    onFollowButtonClicked: (isFollowedByYou: Boolean) -> Unit,
    onWatchlistMovieClicked: (movieId: Int) -> Unit
) {
    val userInfo = fullUserInfo.userInfo
    Column(modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp)) {
        MainProfileInfo(userInfo)
        ProfileFollowingsAndFollowersView(
            fullUserInfo,
            onFollowersClicked,
            onFollowingsClicked,
            Modifier.padding(top = 16.dp)
        )
        if (userInfo.isFollowsYou != null && userInfo.isFollowedByYou != null) {
            FollowButton(userInfo.isFollowsYou, userInfo.isFollowedByYou) {
                onFollowButtonClicked.invoke(userInfo.isFollowedByYou)
            }
        }

        UserDescription(userInfo)
        UserSocialMediaLinks(userInfo, onLinkClicked)

        UserWatchlist(fullUserInfo, onWatchlistMovieClicked)
        UserPostsTitle(fullUserInfo)
    }
}

@Composable
private fun FollowButton(
    isFollowsYou: Boolean, isFollowedByYou: Boolean,
    onFollowButtonClicked: (isFollowedByYou: Boolean) -> Unit
) {
    val text = when {
        isFollowedByYou -> "Отписаться"
        !isFollowedByYou && isFollowsYou -> "Подписаться в ответ"
        else -> "Подписаться"
    }

    Button(
        onClick = { onFollowButtonClicked.invoke(isFollowedByYou) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.h6, modifier = Modifier.padding(8.dp))
    }
}