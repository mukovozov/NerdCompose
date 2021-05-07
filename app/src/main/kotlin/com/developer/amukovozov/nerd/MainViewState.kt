package com.developer.amukovozov.nerd

data class MainViewState(
    val appState: AppState = AppState.Starting
)

enum class AppState {
    Starting,
    Auth,
    AuthInProgress,
    Home
}