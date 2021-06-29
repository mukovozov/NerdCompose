package com.developer.amukovozov.nerd

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.developer.amukovozov.nerd.repository.AuthRepository
import com.developer.amukovozov.nerd.repository.UserDataRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository
) : BaseViewModel() {
    companion object {
        private const val APP_DEEPLINK_SCHEME = "nerd"
        private const val YANDEX_AUTH_DEEPLINK_HOST = "token"
        private const val YANDEX_TOKEN_QUERY_NAME = "access_token"
        private const val YANDEX_START_QUERY_INDICATOR = "#"
        private const val COMMON_START_QUERY_INDICATOR = "?"
    }

    var viewState by mutableStateOf(MainViewState())

    fun appStartedNormally() {
        onAppStarted()
    }

    fun onTokenReceived(deeplink: Uri) {
        if (isFromYandexAuth(deeplink)) {
            val token = extractToken(deeplink)

            authRepository.yandexAuth(token)
                .doOnSubscribe { viewState = viewState.copy(appState = AppState.AuthInProgress) }
                .schedulersIoToMain()
                .subscribe({
                    userDataRepository.putToken(it.token)
                    onAppStarted()
                }, {
                    Timber.e(it)
                    onAppStarted()
                })
                .disposeOnViewModelDestroy()
        }
    }

    private fun extractToken(deeplink: Uri): String {
        val deeplinkString = deeplink.toString()
        return deeplinkString
            .replace(YANDEX_START_QUERY_INDICATOR, COMMON_START_QUERY_INDICATOR)
            .toUri()
            .getQueryParameter(YANDEX_TOKEN_QUERY_NAME) ?: ""
    }

    private fun isFromYandexAuth(deeplink: Uri) = deeplink.scheme == APP_DEEPLINK_SCHEME &&
                deeplink.host == YANDEX_AUTH_DEEPLINK_HOST

    private fun onAppStarted() {
        val isUserAuthorized = !userDataRepository.getToken().isNullOrBlank()
        val nextAppState = if (isUserAuthorized) AppState.Home else AppState.Auth
        viewState = viewState.copy(appState = nextAppState)
    }
}