package com.developer.amukovozov.nerd.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.ui.theme.backgroundColor
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavController,
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
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            Button(onClick = { viewModel.onYandexAuthClicked() }) {
                Text(text = "Войти через Яндекс", style = MaterialTheme.typography.h6)
            }
        }
    }
}