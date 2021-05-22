package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.model.UserInfoDetails
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class UserRepository @Inject constructor(
    private val api: NerdApi
) {

    fun getMyUserInfo(): Single<UserInfoDetails> {
        return api.getUserMe()
    }

    fun getUserInfoById(userId: Int): Single<UserInfoDetails> {
        return api.getUserById(userId)
    }

    fun getMyFollowings(): Single<List<UserInfo>> {
        return api.getMyFollowings()
    }

    fun getUserFollowings(userId: Int): Single<List<UserInfo>> {
        return api.getUserFollowings(userId)
    }

    fun getMyFollowers(): Single<List<UserInfo>> {
        return api.getMyFollowers()
    }

    fun getUserFollowers(userId: Int): Single<List<UserInfo>> {
        return api.getUserFollowers(userId)
    }
}