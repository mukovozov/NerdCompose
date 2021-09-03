package com.developer.amukovozov.nerd.ui.screens.browse

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.model.BrowseInformation
import com.developer.amukovozov.nerd.model.UserInfo
import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.model.movie.Movie
import com.developer.amukovozov.nerd.ui.components.Chip
import com.developer.amukovozov.nerd.ui.components.MovieSearchResultItem
import com.developer.amukovozov.nerd.ui.components.TagsGroup
import com.developer.amukovozov.nerd.ui.components.searchbar.LoadingScreen
import com.developer.amukovozov.nerd.ui.components.searchbar.TextSearchBar
import com.developer.amukovozov.nerd.ui.screens.movie_details.MovieDetailsScreen
import com.developer.amukovozov.nerd.ui.screens.profile.WatchlistCollection
import com.developer.amukovozov.nerd.ui.screens.profile.WatchlistItem
import com.developer.amukovozov.nerd.ui.screens.profile.another.ProfileScreen
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.developer.amukovozov.nerd.ui.theme.white
import com.developer.amukovozov.nerd.util.ui.Content
import com.developer.amukovozov.nerd.util.ui.Loading
import com.developer.amukovozov.nerd.util.ui.Stub
import com.developer.amukovozov.nerd.util.ui.rememberProfilePainter
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun BrowseScreen(
    navController: NavController,
    viewModel: BrowseViewModel,
    modifier: Modifier = Modifier
) {
    val viewState = viewModel.viewState
    Scaffold(
        modifier = modifier.systemBarsPadding(),
        topBar = {
            SearchBar(query = viewState.searchField, onQueryChanged = viewModel::onSearchQueryChanged)
        }
    ) {
        when (viewState.browseInfoState) {
            is Loading -> {
                LoadingScreen()
            }
            is Stub -> {
                // i'll handle it later, i promise
            }
            is Content -> {
                var size by remember { mutableStateOf(Size.Zero) }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        size = coordinates.size.toSize()
                    }) {

                    BrowseContent(
                        viewState = viewState,
                        maxWidth = size.width,
                        onMovieSelected = { movieId ->
                            navController.navigate(MovieDetailsScreen.createDestination(movieId))
                        },
                        onTagSelected = viewModel::onTagSelected,
                        onUserClicked = { userId ->
                            navController.navigate(ProfileScreen.createDestination(userId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BrowseContent(
    viewState: BrowseViewState,
    maxWidth: Float,
    onMovieSelected: (movieId: Int) -> Unit,
    onTagSelected: (Tag) -> Unit,
    onUserClicked: (userId: Int) -> Unit
) {
    val browseInfo = (viewState.browseInfoState as Content).content ?: return
    when {
        viewState.selectedTag != null -> {
//            getTagCoordinates(viewState.selectedTag.id)
//            val endPosition = with(LocalDensity.current) { 64.dp.toPx() } to maxWidth
//
            val tag = viewState.selectedTag
            Box(modifier = Modifier.fillMaxWidth()) {
                Chip(
                    modifier = Modifier.align(Alignment.Center),
                    tag.title,
                    tag.emojiCode,
                    tag.backgroundColor,
                    tag.textColor
                )
            }
        }
        viewState.searchResult.isEmpty() -> {
            DefaultStateScreen(browseInfo, onTagSelected, onUserClicked, onMovieSelected)
        }
        viewState.searchField.isNotEmpty() -> {
            LazyColumn() {
                items(viewState.searchResult) {
                    MovieSearchResultItem(movie = it, onMovieSelected = onMovieSelected::invoke)
                }
            }

        }
    }
}

@Composable
private fun DefaultStateScreen(
    browseInfo: BrowseInformation,
    onTagSelected: (Tag) -> Unit,
    onUserClicked: (userId: Int) -> Unit,
    onMovieClicked: (movieId: Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Поиск по тегам",
            style = MaterialTheme.typography.h5
        )
        TagsGroup(
            modifier = Modifier.padding(top = 8.dp),
            tags = browseInfo.tags,
            onTagClicked = onTagSelected
        )

        Spacer(modifier = Modifier.height(16.dp))
        WatchlistCollection(
            collectionTitle = "Что сейчас смотрят",
            collectionTitleStyle = MaterialTheme.typography.h5,
            items = {
                items(browseInfo.popularMovies) {
                    WatchlistItem(movie = it, onMovieClicked)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        WatchlistCollection(
            collectionTitle = "Они много смотрят",
            collectionTitleStyle = MaterialTheme.typography.h5,
            items = {
                items(browseInfo.users) {
                    UserItem(userInfo = it, onUserClicked = onUserClicked)
                }
            })
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    val view = LocalView.current
    TopAppBar {
        TextSearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            value = query,
            label = stringResource(R.string.feed_creation_movie_text_field_label),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = white,
                focusedBorderColor = primaryColor,
                focusedLabelColor = white
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
    }
}


@Composable
fun UserItem(userInfo: UserInfo, onUserClicked: (userId: Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onUserClicked.invoke(userInfo.id) }
    ) {
        Image(
            painter = rememberProfilePainter(userInfo.avatarPath), contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Text(text = userInfo.nickname, style = MaterialTheme.typography.caption)
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar("") {}
}

@Preview
@Composable
fun DefaultStateScreenPreview() {
    DefaultStateScreen(
        browseInfo = BrowseInformation(
            emptyList(), listOf(
                Tag(1, "perviy", null, "33C22D3D", "FFC22D3D"),
                Tag(2, "vtoroy", null, "3373BFEE", "FF73BFEE"),
                Tag(3, "tretiy", null, "33EECAA4", "FFEECAA4")
            ),
            users = listOf(UserInfo(1, "email@nick.ru", "dryupadich", "123"))
        ), onTagSelected = {}, {}, {}
    )
}

private enum class TagState(val tagId: Int? = null) {
    Selected,
    Default
}