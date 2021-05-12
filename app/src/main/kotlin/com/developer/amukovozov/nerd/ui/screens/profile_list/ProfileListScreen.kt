package com.developer.amukovozov.nerd.ui.screens.profile_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.transform.CircleCropTransformation
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.ui.theme.backgroundAccentColor
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.ui.Content
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.Stub
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

enum class ProfileListType {
    Followings, Followers
}

object ProfileListScreen {
    const val Destination = "profile_list/{profile_list_type}"
    const val ProfileListTypeArgument = "profile_list_type"
}

@Composable
fun ProfileListScreen(
    viewModel: ProfileListViewModel,
    profileListType: ProfileListType,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    viewModel.onStartPage(profileListType)
    Scaffold(
        modifier = modifier
            .navigationBarsPadding(),
        topBar = {
            val tabBarTitleId = if (profileListType == ProfileListType.Followers) {
                R.string.followers_title
            } else {
                R.string.followings_title
            }
            TopAppBar(modifier = Modifier.statusBarsPadding()) {
                Text(
                    text = stringResource(tabBarTitleId),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    ) {
        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let { users ->
                    LazyColumn {
                        items(users) {
                            UserInfoItem(userInfo = it)
                        }
                    }
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
fun UserInfoItem(userInfo: UserInfo) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = backgroundAccentColor,
        elevation = 8.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberCoilPainter(request = userInfo.avatarPath,
                    requestBuilder = {
                        transformations(CircleCropTransformation())
                    }),
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                contentDescription = null
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = userInfo.nickname)
            }
        }
    }
}

@Composable
@Preview
fun UserInfoItemPreview() {
    UserInfoItem(
        userInfo = UserInfo(
            24,
            "email@com.ru",
            "Dryupalini",
            "https://nerdbucket.s3-us-east-2.amazonaws.com/PXL_20200923_215917413.png"
        )
    )
}