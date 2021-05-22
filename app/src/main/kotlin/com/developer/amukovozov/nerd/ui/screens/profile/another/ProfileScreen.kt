package com.developer.amukovozov.nerd.ui.screens.profile.another

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.SocialMediaLink
import com.developer.amukovozov.nerd.ui.screens.feed.FeedReviewItem
import com.developer.amukovozov.nerd.ui.screens.profile.ProfileInformation
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
                screenState.content?.let {
                    ProfileInfo(
                        fullUserInfo = it,
                        onFollowersClicked = {
                            navController.navigate(ProfileListScreen.createDestination(ProfileListType.Followers))
                        },
                        onFollowingsClicked = {
                            navController.navigate(ProfileListScreen.createDestination(ProfileListType.Followings))
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
private fun ProfileInfo(
    fullUserInfo: FullUserInfo,
    onFollowersClicked: () -> Unit,
    onFollowingsClicked: () -> Unit,
    onLinkClicked: (SocialMediaLink) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(top = 16.dp)) {
        item {
            ProfileInformation(
                fullUserInfo = fullUserInfo,
                onFollowersClicked = onFollowersClicked,
                onFollowingsClicked = onFollowingsClicked,
                onLinkClicked = onLinkClicked
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
private fun FollowButton() {

}