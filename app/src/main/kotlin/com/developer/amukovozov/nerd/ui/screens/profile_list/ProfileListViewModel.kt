package com.developer.amukovozov.nerd.ui.screens.profile_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.repository.UserRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    var viewState by mutableStateOf(ProfileListViewState())

    fun onScreenOpened(profileListType: ProfileListType) {
        loadUsers(profileListType)
    }

    private fun loadUsers(profileListType: ProfileListType) {
        val source = if (profileListType == ProfileListType.Followers) {
            userRepository.getMyFollowers()
        } else {
            userRepository.getMyFollowings()
        }

        source.toObservable()
            .map<ScreenState<List<UserInfo>>> { Content(it) }
            .startWithItem(Loading())
            .doOnError(Timber::e)
            .onErrorReturn(::Stub)
            .schedulersIoToMain()
            .subscribe { viewState = viewState.copy(screenState = it) }
            .disposeOnViewModelDestroy()
    }
}