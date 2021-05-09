package com.developer.amukovozov.nerd.ui.screens.profile

import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.ScreenState

data class ProfileViewState(
    val screenState: ScreenState<FullUserInfo> = Loading()
)