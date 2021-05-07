package com.developer.amukovozov.nerd.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.util.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : BaseViewModel() {

    var viewState by mutableStateOf(HomeViewState())

    fun onTabSelected(tab: HomeTab) {
        viewState = viewState.copy(selectedTab = tab)
    }
}