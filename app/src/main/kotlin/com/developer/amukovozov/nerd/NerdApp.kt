package com.developer.amukovozov.nerd

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import androidx.navigation.compose.rememberNavController
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
    homeViewModel: HomeViewModel
) {
    val navController = rememberNavController()

    NerdTheme {
        ProvideWindowInsets {
            Home(
                navController,
                homeViewModel.viewState.selectedTab,
                homeViewModel::onTabSelected
            )
        }
    }
}