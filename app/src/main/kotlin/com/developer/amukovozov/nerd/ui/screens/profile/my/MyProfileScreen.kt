package com.developer.amukovozov.nerd.ui.screens.profile.my

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.SocialMediaLink
import com.developer.amukovozov.nerd.model.UserInfoDetails
import com.developer.amukovozov.nerd.ui.screens.feed.FeedReviewItem
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


object MyProfileScreen {
    const val Destination = "my_profile"
}

@Composable
fun MyProfileScreen(
    viewModel: MyProfileViewModel,
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
                    MyProfileInfo(
                        fullUserInfo = it,
                        onLogoutButtonClicked = viewModel::onLogoutButtonClicked,
                        onEditButtonClicked = viewModel::onEditButtonClicked,
                        onFollowersClicked = { userId ->
                            navController.navigate(ProfileListScreen.createDestination(ProfileListType.Followers, userId))
                        },
                        onFollowingsClicked = { userId ->
                            navController.navigate(ProfileListScreen.createDestination(ProfileListType.Followings, userId))
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
private fun MyProfileInfo(
    fullUserInfo: FullUserInfo,
    onLogoutButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onFollowersClicked: (userId: Int) -> Unit,
    onFollowingsClicked: (userId: Int) -> Unit,
    onLinkClicked: (SocialMediaLink) -> Unit
) {
    LazyColumn {
        item { MyProfileSettingsButtons(onLogoutButtonClicked, onEditButtonClicked) }
        item {  }
        item {
            MyProfileInformation(
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
private fun MyProfileInformation(
    fullUserInfo: FullUserInfo,
    onFollowersClicked: (userId: Int) -> Unit,
    onFollowingsClicked: (userId: Int) -> Unit,
    onLinkClicked: (SocialMediaLink) -> Unit
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

        UserDescription(userInfo)
        UserSocialMediaLinks(userInfo, onLinkClicked)

        UserWatchlist(fullUserInfo)
        UserPostsTitle(fullUserInfo)
    }
}

@Composable
fun MyProfileSettingsButtons(
    onLogoutButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, top = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            modifier = Modifier
                .padding(4.dp)
                .clickable { onEditButtonClicked.invoke() },
            contentDescription = null
        )
        Icon(
            imageVector = Icons.Filled.Logout, contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(4.dp)
                .clickable { onLogoutButtonClicked.invoke() },
        )
    }
}

@Composable
@Preview
fun ProfilePreview() {
    MyProfileInfo(
        FullUserInfo(
            UserInfoDetails("1", "muk@ku.ru", "kitaec", "description", null, null, false, false),
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