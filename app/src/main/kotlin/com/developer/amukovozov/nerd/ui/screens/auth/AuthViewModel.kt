package com.developer.amukovozov.nerd.ui.screens.auth

import com.developer.amukovozov.nerd.util.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
) : BaseViewModel() {

    companion object {
        const val YANDEX_AUTH_LINK =
            "https://oauth.yandex.ru/authorize?response_type=token&client_id=37f8f0c9dbff4bcbb781689b05c773fe"
    }
}