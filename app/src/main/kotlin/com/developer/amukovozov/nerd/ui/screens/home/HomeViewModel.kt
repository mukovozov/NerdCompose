package com.developer.amukovozov.nerd.ui.screens.home

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.developer.amukovozov.nerd.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    companion object {
        private const val START_TOKEN_QUERY_INDICATOR = "access_token="
        private const val END_TOKEN_QUERY_INDICATOR = "&"
    }

    var viewState by mutableStateOf(HomeViewState())

    fun onTabSelected(tab: HomeTab) {
        viewState = viewState.copy(selectedTab = tab)
    }

    fun onTokenReceived(deeplink: String) {
        val startTokenIndex = deeplink.indexOf(START_TOKEN_QUERY_INDICATOR)
        val endTokenIndex = deeplink.indexOf(END_TOKEN_QUERY_INDICATOR)
        val token = deeplink.substring(startTokenIndex, endTokenIndex)
        tokenRepository.putToken(token)
    }
}