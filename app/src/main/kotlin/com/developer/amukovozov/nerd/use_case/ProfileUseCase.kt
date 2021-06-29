package com.developer.amukovozov.nerd.use_case

import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.repository.FeedRepository
import com.developer.amukovozov.nerd.repository.UserDataRepository
import com.developer.amukovozov.nerd.repository.UserRepository
import com.developer.amukovozov.nerd.repository.WatchlistRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class ProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val watchlistRepository: WatchlistRepository,
    private val feedRepository: FeedRepository,
    private val userDataRepository: UserDataRepository
) {

    companion object {
        private const val START_PAGE = 0
    }

    fun getUserProfile(userId: Int): Single<FullUserInfo> {
        return Single.zip(
            userRepository.getUserInfoById(userId),
            userRepository.getUserFollowings(userId),
            userRepository.getUserFollowers(userId),
            watchlistRepository.loadUserWatchlistByPage(START_PAGE, userId),
            feedRepository.loadUserFeedPage(START_PAGE, userId)
        ) { userInfo, followings, followers, watchlist, posts ->
            FullUserInfo(
                userInfo = userInfo,
                followings = followings.size,
                followers = followers.size,
                watchList = watchlist,
                posts = posts
            )
        }
    }

    fun getMyProfile(): Single<FullUserInfo> {
        return Single.zip(
            userRepository.getMyUserInfo(),
            userRepository.getMyFollowings(),
            userRepository.getMyFollowers(),
            watchlistRepository.loadMyWatchlistByPage(START_PAGE),
            feedRepository.loadMyFeedPage(START_PAGE)
        ) { userInfo, followings, followers, watchlist, posts ->
            FullUserInfo(
                userInfo = userInfo,
                followings = followings.size,
                followers = followers.size,
                watchList = watchlist,
                posts = posts
            )
        }.doOnSuccess {
            if (userDataRepository.getMyUserId() == 0) {
                userDataRepository.saveMyUserId(it.userInfo.id.toInt())
            }
        }
    }
}