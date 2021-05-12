package com.developer.amukovozov.nerd.ui.screens.profile_list

import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.ScreenState

data class ProfileListViewState(
    val screenState: ScreenState<List<UserInfo>> = Loading()
)