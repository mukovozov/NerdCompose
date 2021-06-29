package com.developer.amukovozov.nerd.ui.screens.profile.another

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.repository.FollowersRepository
import com.developer.amukovozov.nerd.use_case.ProfileUseCase
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val followersRepository: FollowersRepository
) : BaseViewModel() {

    var viewState by mutableStateOf(ProfileViewState())

    fun onScreenOpened(userId: Int) {
        loadProfile(userId)
    }

    fun onFollowButtonClicked(isFollowedByYou: Boolean, userId: Int) {
        val request = if (isFollowedByYou) {
            followersRepository.unfollow(userId)
        } else {
            followersRepository.follow(userId)
        }

        request.schedulersIoToMain()
            .toObservable<ProfileViewState>()
            .startWithItem(viewState.copy(isFollowButtonLoading = true))
            .subscribe({
                viewState = it
            }, {
                viewState = viewState.copy(isFollowButtonLoading = false)
                Timber.e(it)
            })
            .disposeOnViewModelDestroy()
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