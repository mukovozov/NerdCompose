package com.developer.amukovozov.nerd

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.developer.amukovozov.nerd.ui.screens.auth.AuthScreen
import com.developer.amukovozov.nerd.ui.screens.auth.AuthViewModel
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
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    val viewModel: AppViewModel = viewModel()

    NerdTheme {
        ProvideWindowInsets {
            when (viewModel.viewState.appState) {
                AppState.Starting -> {
                    // splash screen
                    Timber.d("Splash Screen")
                }
                AppState.Auth -> {
                    AuthScreen(authViewModel, navController)
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