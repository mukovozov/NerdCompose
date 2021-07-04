package com.developer.amukovozov.nerd.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.ui.screens.browse.Browse
import com.developer.amukovozov.nerd.ui.screens.feed.Feed
import com.developer.amukovozov.nerd.ui.screens.feed.FeedViewModel
import com.developer.amukovozov.nerd.ui.screens.profile.another.ProfileScreen
import com.developer.amukovozov.nerd.ui.screens.profile.another.ProfileViewModel
import com.developer.amukovozov.nerd.ui.screens.profile.my.MyProfileScreen
import com.developer.amukovozov.nerd.ui.screens.profile.my.MyProfileViewModel
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListScreen
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListType
import com.developer.amukovozov.nerd.ui.screens.profile_list.ProfileListViewModel
import com.developer.amukovozov.nerd.ui.theme.backgroundColor
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Home(
    navController: NavHostController,
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    val navItems = HomeTab.values().toList()
    Scaffold(
        modifier = Modifier
            .background(backgroundColor)
            .navigationBarsPadding(),
        topBar = {
            if (selectedTab != HomeTab.Profile) {
                TopAppBar(modifier = Modifier.statusBarsPadding()) {
                    Text(
                        text = stringResource(id = R.string.feed_app_bar_title),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        },
        bottomBar = {
//            val navBackStackEntry by navController.currentBackStackEntryAsState()
//            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE) ?: HomeTab.Feed.route
//            if (HomeTab.values().find { it.route == currentRoute } != null) {
            BottomNavigation {
                navItems.forEach { screen ->
                    val isScreenSelected = screen.route == selectedTab.route
                    NerdBottomNavigationItem(isScreenSelected, screen, onTabSelected, navController)
                }
//                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = HomeTab.Feed.route) {
            composable(HomeTab.Feed.route) {
                val viewModel = hiltNavGraphViewModel<FeedViewModel>()
                Feed(viewModel, navController, Modifier.padding(innerPadding))
            }
            composable(HomeTab.Search.route) { Browse(navController) }
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
                popUpTo = navController.graph.startDestination
                launchSingleTop = true
            }
        }
    )
}

enum class HomeTab(val route: String, val inactiveIcon: ImageVector, val activeIcon: ImageVector) {
    Feed("feed", Icons.Outlined.Home, Icons.Filled.Home),
    Search("search", Icons.Outlined.Search, Icons.Filled.Search),
    Profile("profile", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle);
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
        arguments = listOf(navArgument(ProfileListScreen.ProfileListTypeArgument) {
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
