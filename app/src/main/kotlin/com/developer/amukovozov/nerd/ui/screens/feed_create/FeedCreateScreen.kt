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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.emoji.text.EmojiCompat
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.ui.components.GridLazyLayout
import com.developer.amukovozov.nerd.ui.components.GridType
import com.developer.amukovozov.nerd.ui.components.hexToColor
import com.developer.amukovozov.nerd.ui.components.searchbar.OutlinedTextSearchBar
import com.developer.amukovozov.nerd.ui.components.searchbar.autocomplete.AutoCompleteState
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.ui.theme.progressIndicatorBackground
import com.developer.amukovozov.nerd.ui.theme.whiteAlpha
import com.developer.amukovozov.nerd.util.ui.rememberTmdbPosterPainter
import com.google.accompanist.insets.systemBarsPadding

object FeedCreateScreen {
    private const val Route = "feed_create"
    const val Argument = "id"
    const val Destination = "$Route?$Argument={$Argument}"

    fun createDestination(movieId: Int) = "${Route}?$Argument=$movieId"
}

private val CircleTagSize = 64.dp
private val MoviePosterHeight = 75.dp
private val MoviePosterWidth = 50.dp

@Composable
fun FeedCreateScreen(
    movieId: Int,
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
//            val buttonText = if (viewModel.viewState.progressIndex == 2) "Опубликовать" else "Продолжить"
//            Button(
//                onClick = { viewModel.onStepFinished() },
//                enabled = viewModel.viewState.isNextButtonEnabled,
//                colors = ButtonDefaults.buttonColors(
//                  backgroundColor = primaryColor
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//            ) {
//                Text(
//                    text = buttonText,
//                    style = MaterialTheme.typography.h6
//                )
//            }
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
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
                        ThirdStep(reviewState = viewState.review, viewModel::onReviewChanged)
                    }
                }
            }
            val buttonText = if (viewModel.viewState.progressIndex == 2) "Опубликовать" else "Продолжить"
            Button(
                onClick = { viewModel.onStepFinished() },
                enabled = viewModel.viewState.isNextButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                  backgroundColor = primaryColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.h6
                )
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
                        modifier = Modifier.align(Alignment.Center)
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
            stringResource(R.string.feed_creation_add_movie_title),
            style = MaterialTheme.typography.h6
        )
        val view = LocalView.current

        OutlinedTextSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = autoCompleteState.query,
            label = stringResource(R.string.feed_creation_movie_text_field_label),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = primaryColor,
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
    onTagSelected: (tag: Tag, isSelected: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberTmdbPosterPainter(movie?.posterPath),
                modifier = Modifier
                    .height(MoviePosterHeight)
                    .width(MoviePosterWidth),
                contentDescription = null
            )
            Text(
                movie?.title ?: "",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.subtitle1
            )
        }
        Text(
            stringResource(R.string.feed_creation_add_tags_title),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.h6
        )
        val columns = 4
        GridLazyLayout(gridType = GridType.Fixed(columns)) {
            items(allTags.size) { index ->
                val tagState = allTags[index]
                CircleTagItem(tagState, onTagSelected)
            }
        }
    }
}

@Composable
private fun ThirdStep(
    reviewState: TextFieldState,
    onTextChanged: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            stringResource(R.string.feed_creation_add_review_title),
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            value = reviewState.text,
            onValueChange = {
                onTextChanged.invoke(it)
//                reviewState.text = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = primaryColor,
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor
            ),
            label = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(R.string.feed_creation_review_text_field_label),
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

@Composable
fun CircleTagItem(tagState: TagState, onTagSelected: (tag: Tag, isSelected: Boolean) -> Unit) {
    val (tag, isSelected) = tagState
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(CircleTagSize)
                .padding(4.dp)
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(50))
                .background(tag.backgroundColor.hexToColor(), RoundedCornerShape(50))
                .clickable { onTagSelected.invoke(tag, !isSelected) }
        ) {
            tag.emojiCode?.let {
                val processed = EmojiCompat.get().process(tag.emojiCode)
                Text(
                    text = processed.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier
                        .size(CircleTagSize)
                        .background(whiteAlpha, RoundedCornerShape(50))

                )
            }
        }
        val tColor = tag.textColor.hexToColor()
        Text(
            text = tag.title,
            color = tColor,
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption
        )
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

@Preview
@Composable
fun TagItemPreview() {
    TagItem(
        tagState = TagState(Tag(1, "Colorful", "\uD83D\uDE0D", "33C22D3D", "FFC22D3D"), isSelected = true),
        onTagSelected = { _, _ -> })
}

@Preview
@Composable
fun CircleTagItemPreview() {
    CircleTagItem(
        tagState = TagState(Tag(1, "Amazing", "\uD83D\uDE0D", "33C22D3D", "FFC22D3D"), isSelected = false),
        onTagSelected = { _, _ -> })
}

// probably should be removed
@Composable
fun TagItem(tagState: TagState, onTagSelected: (tag: Tag, isSelected: Boolean) -> Unit) {
    val (tag, isSelected) = tagState
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
//            .height(100.dp)
            .padding(4.dp)
            .background(tag.backgroundColor.hexToColor(), RoundedCornerShape(50))
            .clickable { onTagSelected.invoke(tag, !isSelected) }
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            tag.emojiCode?.let {
                val processed = EmojiCompat.get().process(tag.emojiCode)
                Text(text = processed.toString(), textAlign = TextAlign.Center)
                Spacer(Modifier.width(4.dp))
            }

            val tColor = tag.textColor.hexToColor()
            Text(
                text = tag.title,
                color = tColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier
                    .size(16.dp)
                    .offset(x = 4.dp, y = (-8).dp)
                    .align(Alignment.TopEnd)
                    .background(whiteAlpha, RoundedCornerShape(50))

            )
        }
    }
}
