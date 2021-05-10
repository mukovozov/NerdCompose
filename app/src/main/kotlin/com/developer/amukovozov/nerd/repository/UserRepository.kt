package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.FullUserInfo
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class UserRepository @Inject constructor(
    private val api: NerdApi
) {
    fun getMyProfile(): Single<FullUserInfo> {
        return Single.zip(
            api.getUserMe(),
            getMyFollowings(),
            getMyFollowers()
        ) { userInfo, followings, followers ->
            FullUserInfo(userInfo = userInfo, followings = followings.size, followers = followers.size)
        }
    }

    private fun getMyFollowings(): Single<List<UserInfo>> {
        return api.getMyFollowings()
    }

    private fun getMyFollowers(): Single<List<UserInfo>> {
        return api.getMyFollowers()
    }
}