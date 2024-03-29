package com.developer.amukovozov.nerd.ui.components.searchbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.developer.amukovozov.nerd.ui.theme.primaryColor

@Composable
fun LoadingScreen(
    progressBarColor: Color = primaryColor
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = progressBarColor)
    }
}