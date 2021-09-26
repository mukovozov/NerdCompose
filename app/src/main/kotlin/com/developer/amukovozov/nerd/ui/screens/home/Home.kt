package com.developer.amukovozov.nerd.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.ui.screens.browse.BrowseScreen
import com.developer.amukovozov.nerd.ui.screens.browse.BrowseViewModel
import com.developer.amukovozov.nerd.ui.screens.feed.Feed
import com.developer.amukovozov.nerd.ui.screens.feed.FeedScreen
import com.developer.amukovozov.nerd.ui.screens.feed.FeedViewModel
import com.developer.amukovozov.nerd.ui.screens.feed_create.FeedCreateScreen
import com.developer.amukovozov.nerd.ui.screens.feed_create.FeedCreateViewModel
import com.developer.amukovozov.nerd.ui.screens.movie_details.MovieDetailsScreen
import com.developer.amukovozov.nerd.ui.screens.movie_details.MovieDetailsViewModel
import com.developer.amukovozov.nerd.ui.screens.profile.another.ProfileScreen
import com.developer.amukovozov.nerd.ui.screens.profile.another.ProfileViewModel
import com.developer.amukovozov.nerd.ui.screens.profile.my.MyProfileScreen
import com.developer.amukovozov.nerd.ui.screens.profile.my.MyProfileViewModel
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListScreen
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListType
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListViewModel
import com.developer.amukovozov.nerd.ui.theme.backgroundColor
import com.developer.amukovozov.nerd.ui.theme.primaryColor
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Home(
    navController: NavHostController,
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    val navItems = HomeTab.values().toList()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: HomeTab.Feed.route

    Scaffold(
        modifier = Modifier
            .background(backgroundColor)
            .navigationBarsPadding(),
        topBar = {
            if (selectedTab != HomeTab.Profile &&
                selectedTab != HomeTab.Search &&
                currentRoute != MovieDetailsScreen.Destination &&
                currentRoute != FeedCreateScreen.Destination
            ) {
                TopAppBar(modifier = Modifier.statusBarsPadding()) {
                    Text(
                        text = stringResource(id = R.string.feed_app_bar_title),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentRoute != MovieDetailsScreen.Destination &&
                currentRoute != FeedCreateScreen.Destination
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(FeedCreateScreen.createDestination(0))
                    },
                    shape = RoundedCornerShape(50),
                    backgroundColor = primaryColor
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        },
        bottomBar = {
            if (currentRoute != MovieDetailsScreen.Destination &&
                currentRoute != FeedCreateScreen.Destination &&
                currentRoute != ProfileScreen.Destination
            ) {
                BottomNavigation {
                    navItems.forEach { screen ->
                        val isScreenSelected = screen.route == selectedTab.route
                        NerdBottomNavigationItem(isScreenSelected, screen, onTabSelected, navController)
                    }
                }

            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = FeedScreen.Destination) {
            navigation(HomeTab.Feed.route, FeedScreen.Destination) {
                feedNestedNavigation(navController, innerPadding)
            }
            composable(HomeTab.Search.route) {
                val viewModel = hiltViewModel<BrowseViewModel>()
                BrowseScreen(navController, viewModel)
            }
            navigation(HomeTab.Profile.route, MyProfileScreen.Destination) {
                profileNestedNavigation(navController, innerPadding)
            }
        }
    }
}

@Composable
private fun RowScope.NerdBottomNavigationItem(
    isScreenSelected: Boolean,
    screen: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    navController: NavHostController
) {
    BottomNavigationItem(
        modifier = Modifier.padding(bottom = 8.dp),
        icon = {
            val icon = if (isScreenSelected) screen.activeIcon else screen.inactiveIcon
            Icon(imageVector = icon, contentDescription = null)
        },
        selected = isScreenSelected,
        onClick = {
            onTabSelected.invoke(screen)

            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

enum class HomeTab(val route: String, val inactiveIcon: ImageVector, val activeIcon: ImageVector) {
    Feed("feed_tab", Icons.Outlined.Home, Icons.Filled.Home),
    Search("search", Icons.Outlined.Search, Icons.Filled.Search),
    Profile("profile", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle);
}

private fun NavGraphBuilder.feedNestedNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    composable(HomeTab.Feed.route) {
        val viewModel = hiltViewModel<FeedViewModel>()
        Feed(viewModel, navController, Modifier.padding(innerPadding))
    }

    composable(
        MovieDetailsScreen.Destination,
        arguments = listOf(
            navArgument(MovieDetailsScreen.Argument) {
                type = NavType.IntType
            }
        )
    ) {
        val viewModel = hiltViewModel<MovieDetailsViewModel>()
        val movieId = it.arguments?.getInt(MovieDetailsScreen.Argument) ?: return@composable

        MovieDetailsScreen(
            movieId = movieId,
            viewModel = viewModel,
            navController = navController
        )
    }
    composable(
        FeedCreateScreen.Destination,
        arguments = listOf(
            navArgument(FeedCreateScreen.Argument) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        val viewModel: FeedCreateViewModel = hiltViewModel()
        val movieId = it.arguments?.getInt(FeedCreateScreen.Argument) ?: return@composable

        FeedCreateScreen(
            movieId = movieId,
            viewModel = viewModel,
            navController = navController
        )
    }
}

private fun NavGraphBuilder.profileNestedNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    composable(HomeTab.Profile.route) {
        val viewModel = hiltNavGraphViewModel<MyProfileViewModel>()
        MyProfileScreen(viewModel, navController, Modifier.padding(innerPadding))
    }
    composable(
        ProfileListScreen.Destination,
        arguments = listOf(
            navArgument(ProfileListScreen.ProfileListTypeArgument) {
                type = NavType.StringType
            },
            navArgument(ProfileListScreen.UserIdTypeArgument) {
                type = NavType.IntType
            })
    ) {
        val viewModel = hiltNavGraphViewModel<ProfileListViewModel>()
        val profileListType: ProfileListType = it.arguments?.getString(ProfileListScreen.ProfileListTypeArgument)
            .run {
                if (this == ProfileListType.Followers.name) {
                    ProfileListType.Followers
                } else {
                    ProfileListType.Followings
                }
            }
        val userId = it.arguments?.getInt(ProfileListScreen.UserIdTypeArgument)
        ProfileListScreen(
            viewModel = viewModel,
            navController = navController,
            profileListType = profileListType,
            userId = userId
        )
    }
    composable(
        ProfileScreen.Destination,
        arguments = listOf(navArgument(ProfileScreen.Argument) {
            type = NavType.IntType
        })
    ) {
        val viewModel = hiltNavGraphViewModel<ProfileViewModel>()
        val userId = it.arguments?.getInt(ProfileScreen.Argument) ?: return@composable

        ProfileScreen(userId, viewModel, navController)
    }
}
