package com.developer.amukovozov.nerd.use_case

import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.repository.UserRepository
import com.developer.amukovozov.nerd.repository.WatchlistRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class ProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val watchlistRepository: WatchlistRepository
) {

    companion object {
        private const val START_PAGE = 0
    }

    fun getMyProfile(): Single<FullUserInfo> {
        return Single.zip(
            userRepository.getMyUserInfo(),
            userRepository.getMyFollowings(),
            userRepository.getMyFollowers(),
            watchlistRepository.loadMyWatchlistByPage(START_PAGE)
        ) { userInfo, followings, followers, watchlist ->
            FullUserInfo(
                userInfo = userInfo,
                followings = followings.size,
                followers = followers.size,
                watchList = watchlist
            )
        }
    }
}