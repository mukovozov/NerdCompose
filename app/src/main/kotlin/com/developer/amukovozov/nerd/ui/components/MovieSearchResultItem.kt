package com.developer.amukovozov.nerd.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter

private val MoviePosterHeight = 75.dp
private val MoviePosterWidth = 50.dp

@Composable
fun MovieSearchResultItem(movie: Movie, onMovieSelected: (movieId: Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onMovieSelected.invoke(movie.id) }
    ) {
        Image(
            painter = rememberTmdbPosterPainter(movie.posterPath),
            modifier = Modifier
                .height(MoviePosterHeight)
                .width(MoviePosterWidth),
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = movie.title, style = MaterialTheme.typography.subtitle2)
        }
    }
}