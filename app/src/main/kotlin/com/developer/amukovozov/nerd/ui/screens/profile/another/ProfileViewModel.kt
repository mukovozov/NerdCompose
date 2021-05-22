package com.developer.amukovozov.nerd.ui.screens.profile.another

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.use_case.ProfileUseCase
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
) : BaseViewModel() {

    var viewState by mutableStateOf(ProfileViewState())

    fun onScreenOpened(userId: Int) {
        loadProfile(userId)
    }

    private fun loadProfile(userId: Int) {
        profileUseCase.getUserProfile(userId)
            .toObservable()
            .map<ScreenState<FullUserInfo>> { Content(it) }
            .startWithItem(Loading())
            .doOnError(Timber::e)
            .onErrorReturn(::Stub)
            .schedulersIoToMain()
            .subscribe { viewState = viewState.copy(screenState = it) }
            .disposeOnViewModelDestroy()
    }

}