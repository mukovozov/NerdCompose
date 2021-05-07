package com.developer.amukovozov.nerd.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.developer.amukovozov.nerd.ui.theme.backgroundColor
import com.developer.amukovozov.nerd.util.openInChromeTab
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(backgroundColor)
            .navigationBarsPadding()
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            val context = LocalContext.current
            Button(onClick = { openInChromeTab(context, AuthViewModel.YANDEX_AUTH_LINK) }) {
                Text(text = "Войти через Яндекс", style = MaterialTheme.typography.h6)
            }
        }
    }
}