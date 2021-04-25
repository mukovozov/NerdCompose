package com.developer.amukovozov.nerd.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    var viewState by mutableStateOf(HomeViewState())

    fun onTabSelected(tab: HomeTab) {
        viewState = viewState.copy(selectedTab = tab)
    }
}