package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.TokenInfo
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class AuthRepository @Inject constructor(
    private val api: NerdApi
) {
    fun yandexAuth(yaToken: String): Single<TokenInfo> {
        return api.yandexAuth(yaToken)
    }
}