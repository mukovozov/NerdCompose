package com.developer.amukovozov.nerd

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.navigation.compose.rememberNavController
import com.developer.amukovozov.nerd.ui.screens.auth.AuthScreen
import com.developer.amukovozov.nerd.ui.screens.home.Home
import com.developer.amukovozov.nerd.ui.screens.home.HomeViewModel
import com.developer.amukovozov.nerd.ui.theme.NerdTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NerdApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}

@Composable
fun NerdApp(
    viewModel: MainViewModel,
    homeViewModel: HomeViewModel
) {
    val navController = rememberNavController()

    NerdTheme {
        ProvideWindowInsets {
            when (viewModel.viewState.appState) {
                AppState.Starting -> {
                    Timber.d("Splash Screen")
                }
                AppState.Auth -> {
                    AuthScreen()
                }
                AppState.AuthInProgress -> {
                    Timber.d("Show progress bar")
                }
                AppState.Home -> {
                    Home(
                        navController,
                        homeViewModel.viewState.selectedTab,
                        homeViewModel::onTabSelected
                    )
                }
            }
        }
    }
}