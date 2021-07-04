package com.developer.amukovozov.nerd.ui.screens.movie_details

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.util.ui.Content
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.Stub
import com.developer.amukovozov.nerd.util.ui.getContext
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

object MovieDetailsScreen {
    private const val Route = "movie_details"
    const val Argument = "id"
    const val Destination = "$Route/{$Argument}"

    fun createDestination(movieId: Int) = "$Route/$movieId"
}


@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel,
    modifier: Modifier = Modifier
) {
    viewModel.onScreenOpened(movieId)

    Scaffold(
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { innerPadding ->
        val context = getContext()

        when (val screenState = viewModel.viewState.screenState) {
            is Content -> {
                screenState.content?.let {
                    Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()
                }
            }
            is Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is Stub -> {
                // todo show stub
            }
        }
    }
}