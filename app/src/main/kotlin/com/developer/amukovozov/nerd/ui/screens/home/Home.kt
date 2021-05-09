package com.developer.amukovozov.nerd.ui.screens.home

import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import com.developer.amukovozov.nerd.R
import com.developer.amukovozov.nerd.ui.screens.browse.Browse
import com.developer.amukovozov.nerd.ui.screens.feed.Feed
import com.developer.amukovozov.nerd.ui.screens.feed.FeedViewModel
import com.developer.amukovozov.nerd.ui.screens.profile.ProfileScreen
import com.developer.amukovozov.nerd.ui.screens.profile.ProfileViewModel
import com.developer.amukovozov.nerd.ui.theme.backgroundColor
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

private const val KEY_ROUTE = "key_route"

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
            TopAppBar(modifier = Modifier.statusBarsPadding()) {
                Text(
                    text = stringResource(id = R.string.feed_app_bar_title),
                    style = MaterialTheme.typography.h5
                )
            }
        },
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE) ?: HomeTab.Feed.route

                navItems.forEach { screen ->
                    val isScreenSelected = screen.route == selectedTab.route
                    NerdBottomNavigationItem(isScreenSelected, screen, onTabSelected, navController)
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = HomeTab.Feed.route) {
            composable(HomeTab.Feed.route) {
                val viewModel = hiltNavGraphViewModel<FeedViewModel>()
                Feed(viewModel, navController, Modifier.padding(innerPadding))
            }
            composable(HomeTab.Search.route) { Browse(navController) }
            composable(HomeTab.Profile.route) {
                val viewModel = hiltNavGraphViewModel<ProfileViewModel>()
                ProfileScreen(viewModel, navController)
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
