package com.developer.amukovozov.nerd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.repository.TokenRepository
import com.developer.amukovozov.nerd.util.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : BaseViewModel() {
    var viewState by mutableStateOf(AppViewState())

    init {
        onAppStarted()
    }

    private fun onAppStarted() {
        val isUserAuthorized = !tokenRepository.getToken().isNullOrBlank()
        val nextAppState = if (isUserAuthorized) AppState.Home else AppState.Auth
        viewState = viewState.copy(appState = nextAppState)
    }
}