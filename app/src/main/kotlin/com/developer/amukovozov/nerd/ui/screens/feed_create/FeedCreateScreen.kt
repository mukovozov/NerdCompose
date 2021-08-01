package com.developer.amukovozov.nerd.ui.screens.feed_create

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.emoji.text.EmojiCompat
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.ui.components.hexToColor
import com.developer.amukovozov.nerd.ui.components.searchbar.TextSearchBar
import com.developer.amukovozov.nerd.ui.components.searchbar.autocomplete.AutoCompleteState
import com.developer.amukovozov.nerd.ui.screens.feed.FeedScreen
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.ui.theme.progressIndicatorBackground
import com.developer.amukovozov.nerd.ui.theme.white
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.Job

object FeedCreateScreen {
    private const val Route = "feed_create"
    const val Argument = "id"
    const val Destination = "${Route}/{$Argument}"

    fun createDestination(movieId: Int) = "${Route}/$movieId"
}

private val DELAY_TIME = 2000L
private var job: Job? = null

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FeedCreateScreen(
    movieId: Int?,
    viewModel: FeedCreateViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    viewModel.onScreenOpened(movieId)

    if (viewModel.viewState.isReviewPublished) {
        navController.navigateUp()
    }

    Scaffold(modifier = modifier.systemBarsPadding(),
        topBar = {
            FeedCreationTopBar(
                progressIndex = viewModel.viewState.progressIndex,
                totalStepsIndex = viewModel.viewState.totalStepsIndex
            ) {
                navController.navigateUp()
            }
        },
        bottomBar = {
            val buttonText = if (viewModel.viewState.progressIndex == 2) "Опубликовать" else "Продолжить"
            Button(
                onClick = { viewModel.onStepFinished() },
                enabled = viewModel.viewState.isNextButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.h6
                )
            }
        }) {
        val viewState = viewModel.viewState
        when (viewState.progressIndex) {
            0 -> {
                // first step
                // choose movie
                FirstStep(
                    autoCompleteState = viewState.autoCompleteState,
                    onQueryChanged = viewModel::onQueryChanged,
                    onMovieSelected = viewModel::onMovieSelected
                )
            }
            1 -> {
                // second step
                // add tags
                SecondStep(viewState.movie, viewState.tags, viewModel::onTagSelected)
            }
            2 -> {
                // third step
                // write review
                ThirdStep(reviewState = viewState.review)
            }
        }
    }
}

@Composable
private fun FeedCreationTopBar(
    progressIndex: Int,
    totalStepsIndex: Int,
    onBackPressed: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TopAppBarTitle(
                progressIndex = progressIndex, totalStepsCount = totalStepsIndex,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .align(Alignment.Center)
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = onBackPressed,
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        val animatedProgress by animateFloatAsState(
            targetValue = (progressIndex + 1) / totalStepsIndex.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            color = primaryColor,
            backgroundColor = MaterialTheme.colors.progressIndicatorBackground
        )
    }
}

@Composable
private fun TopAppBarTitle(
    progressIndex: Int,
    totalStepsCount: Int,
    modifier: Modifier = Modifier
) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val totalStyle = MaterialTheme.typography.caption.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${progressIndex + 1}")
        }
        withStyle(style = totalStyle) {
            append(stringResource(R.string.feed_creation_total_steps_title, totalStepsCount))
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}

@ExperimentalAnimationApi
@Composable
fun FirstStep(
    autoCompleteState: AutoCompleteState<Movie>,
    onQueryChanged: (query: String) -> Unit,
    onMovieSelected: (movie: Movie) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "Про что расскажешь?",
            style = MaterialTheme.typography.h6
        )
        val view = LocalView.current

        TextSearchBar(
            modifier = Modifier
                .testTag("AutoCompleteSearchBarTag")
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = autoCompleteState.query,
            label = "Фильм",
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor
            ),
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                onQueryChanged("")
                view.clearFocus()
            },
            onValueChanged = { query ->
                onQueryChanged.invoke(query)
            }
        )

        if (autoCompleteState.query != "") {
            LazyColumn {
                items(autoCompleteState.filteredItems) {
                    MovieAutoCompleteItem(movie = it, onMovieSelected)
                }
            }
        }
    }
}

@Composable
private fun SecondStep(
    movie: Movie?,
    allTags: List<TagState>,
    onTagSelected: (tag: Tag, isSelected: Boolean) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberTmdbPosterPainter(movie?.posterPath),
                modifier = Modifier
                    .height(75.dp)
                    .width(50.dp),
                contentDescription = null
            )
            Text(
                movie?.title ?: "",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.subtitle1
            )
        }
        Text(
            "Добавь теги",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.h6
        )
        val columns = 3
        val rows = (allTags.size + columns - 1) / columns
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(rows) { rowIndex ->
                Row {
                    for (columnIndex in 0 until columns) {
                        val itemIndex = rowIndex * columns + columnIndex
                        if (itemIndex < allTags.size) {
                            Box(
                                modifier = Modifier.weight(1f, fill = true),
                                propagateMinConstraints = true
                            ) {
                                val tagState = allTags[itemIndex]
                                TagItem(tag = tagState.tag, tagState.isSelected, onTagSelected)
                            }
                        } else {
                            Spacer(Modifier.weight(1f, fill = true))
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun ThirdStep(
    reviewState: TextFieldState,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            "И пару слов о фильме...",
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            value = reviewState.text,
            onValueChange = {
                reviewState.text = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor
            ),
            label = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = "Ревью",
                        style = MaterialTheme.typography.body2
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(top = 16.dp),
            textStyle = MaterialTheme.typography.body2,
            isError = reviewState.showErrors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeAction()
                }
            )
        )
    }
}

@Composable
fun MovieAutoCompleteItem(movie: Movie, onMovieSelected: (movie: Movie) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onMovieSelected.invoke(movie) }
    ) {
        Image(
            painter = rememberTmdbPosterPainter(movie.posterPath),
            modifier = Modifier
                .height(75.dp)
                .width(50.dp),
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

@Composable
fun TagItem(tag: Tag, selected: Boolean, onTagSelected: (tag: Tag, isSelected: Boolean) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(100.dp)
            .padding(4.dp)
            .background(tag.backgroundColor.hexToColor(), RoundedCornerShape(50))
            .clickable { onTagSelected.invoke(tag, !selected) }
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tag.emojiCode?.let {
                val processed = EmojiCompat.get().process(tag.emojiCode)
                Text(text = processed.toString())
                Spacer(Modifier.width(4.dp))
            }

            val tColor = tag.textColor.hexToColor()
            Text(
                text = tag.title,
                color = tColor, style = MaterialTheme.typography.caption
            )
        }
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = tag.textColor.hexToColor(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(white, RoundedCornerShape(50))
                    .alpha(0.3f)
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun FeedCreateScreenPreview() {
    FirstStep(
        autoCompleteState = AutoCompleteState(
            listOf(
                Movie(12, "", "Lion of love"),
                Movie(12, "", "Terminator")
            ), "query"
        ),
        onQueryChanged = {},
        {}
    )
}