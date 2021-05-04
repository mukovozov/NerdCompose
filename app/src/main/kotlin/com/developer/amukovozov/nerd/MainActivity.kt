package com.developer.amukovozov.nerd

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.developer.amukovozov.nerd.ui.screens.auth.AuthViewModel
import com.developer.amukovozov.nerd.ui.screens.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val APP_DEEPLINK_SCHEME = "nerd"
        private const val AUTH_DEEPLINK_HOST = "token"
    }

    private val homeViewModel by viewModels<HomeViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: Uri? = intent?.data

        if (data != null && data.scheme == APP_DEEPLINK_SCHEME &&
            data.host == AUTH_DEEPLINK_HOST
        ) {
            homeViewModel.onTokenReceived(data.toString())
        }
        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NerdApp(homeViewModel, authViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}