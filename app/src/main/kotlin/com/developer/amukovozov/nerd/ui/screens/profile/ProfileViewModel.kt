package com.developer.amukovozov.nerd.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.repository.UserRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    var viewState by mutableStateOf(ProfileViewState())

    init {
        loadMyProfile()
    }

    private fun loadMyProfile() {
        userRepository.getMyProfile()
            .toObservable()
            .map<ScreenState<FullUserInfo>> { Content(it) }
            .startWithItem(Loading())
            .onErrorReturn(::Stub)
            .doOnError(Timber::e)
            .schedulersIoToMain()
            .subscribe { viewState = viewState.copy(screenState = it) }
            .disposeOnViewModelDestroy()
    }
}