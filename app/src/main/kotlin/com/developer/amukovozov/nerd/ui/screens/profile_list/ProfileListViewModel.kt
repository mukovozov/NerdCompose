package com.developer.amukovozov.nerd.ui.screens.profile_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.repository.UserDataRepository
import com.developer.amukovozov.nerd.repository.UserRepository
import com.developer.amukovozov.nerd.ui.screens.profile.another.ProfileScreen
import com.developer.amukovozov.nerd.ui.screens.profile.my.MyProfileScreen
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository
) : BaseViewModel() {
    var viewState by mutableStateOf(ProfileListViewState())

    fun onScreenOpened(userId: Int?, profileListType: ProfileListType) {
        val userSource = if (userId == null) {
            getMyUsersSource(profileListType)
        } else {
            getUsersSource(userId, profileListType)
        }

        loadUsers(userSource)
    }

    // TODO REMOVE THIS BULLSHIT AND IMPLEMENT EVENT LIVE DATA
    // OR FIND A BETTER SOLUTION
    fun getDestination(userId: Int): String =
        if (userDataRepository.getMyUserId() == userId) {
            MyProfileScreen.Destination
        } else {
            ProfileScreen.createDestination(userId)
        }

    private fun getMyUsersSource(profileListType: ProfileListType): Single<List<UserInfo>> {
        return if (profileListType == ProfileListType.Followers) {
            userRepository.getMyFollowers()
        } else {
            userRepository.getMyFollowings()
        }
    }

    private fun getUsersSource(userId: Int, profileListType: ProfileListType): Single<List<UserInfo>> {
        return if (profileListType == ProfileListType.Followers) {
            userRepository.getUserFollowers(userId)
        } else {
            userRepository.getUserFollowings(userId)
        }
    }

    private fun loadUsers(source: Single<List<UserInfo>>) {
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