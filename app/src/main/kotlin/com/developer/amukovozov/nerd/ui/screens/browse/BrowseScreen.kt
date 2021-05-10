package com.developer.amukovozov.nerd.ui.screens.browse

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.R

@Composable
fun Browse(navController: NavController, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.home_browse),
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
    )
}