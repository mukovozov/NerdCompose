package com.developer.amukovozov.nerd

data class AppViewState(
    val appState: AppState = AppState.Starting
)

enum class AppState {
    Starting,
    Auth,
    Home
}