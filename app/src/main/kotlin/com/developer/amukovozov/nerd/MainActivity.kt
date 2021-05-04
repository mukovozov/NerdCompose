package com.developer.amukovozov.nerd

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
    private val homeViewModel by viewModels<HomeViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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