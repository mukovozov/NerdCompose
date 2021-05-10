package com.developer.amukovozov.nerd.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.transform.CircleCropTransformation
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.ProfileDetailsResponse
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.ui.Content
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.Stub
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

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
        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let {
                    ProfileInfo(
                        fullUserInfo = it,
                        onLogoutButtonClicked = viewModel::onLogoutButtonClicked,
                        onEditButtonClicked = viewModel::onEditButtonClicked
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
    modifier: Modifier = Modifier
) {
    val userInfo = fullUserInfo.userInfo
    ConstraintLayout(modifier) {
        val (iconLogout, iconEdit, avatar,
            nickname, followersAndFollowings,
            description, linksList
        ) = createRefs()

        Icon(imageVector = Icons.Filled.Logout, contentDescription = null,
            modifier = Modifier.constrainAs(iconLogout) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })
        Icon(
            imageVector = Icons.Filled.Edit, contentDescription = null,
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
            style = MaterialTheme.typography.h6,
            color = primaryColor,
            modifier = Modifier.constrainAs(nickname) {
                width = Dimension.fillToConstraints
                top.linkTo(avatar.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(followersAndFollowings) {
                top.linkTo(nickname.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            }
        ) {
            Text(
                text = "${fullUserInfo.followers}\n подписчиков",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .border(width = 2.dp, primaryColor, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            Text(
                text = "${fullUserInfo.followings}\n подписок",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .border(width = 2.dp, primaryColor, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }
        userInfo.description?.let {
            Text(text = it,
                modifier = Modifier.constrainAs(description) {
                    width = Dimension.fillToConstraints
                    top.linkTo(followersAndFollowings.bottom, margin = 24.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                })
        }
    }
}

@Composable
@Preview
fun ProfilePreview() {
    ProfileInfo(
        FullUserInfo(
            ProfileDetailsResponse("1", "muk@ku.ru", "kitaec", "description", null, null),
            5,
            56
        ),
        {},
        {}
    )
}