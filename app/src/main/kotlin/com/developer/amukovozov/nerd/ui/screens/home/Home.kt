package com.developer.amukovozov.nerd.ui.screens.home

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import com.developer.amukovozov.nerd.ui.screens.Browse
import com.developer.amukovozov.nerd.ui.screens.feed.Feed
import com.developer.amukovozov.nerd.ui.screens.Profile

private const val KEY_ROUTE = "key_route"

@Composable
fun Home(
    navController: NavHostController,
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    val navItems = HomeTab.values().toList()
    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.height(56.dp + 8.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE) ?: HomeTab.Feed.route

                navItems.forEach { screen ->
                    val isScreenSelected = screen.route == selectedTab.route
                    NerdBottomNavigationItem(isScreenSelected, screen, onTabSelected, navController)
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = HomeTab.Feed.route) {
            composable(HomeTab.Feed.route) { Feed(navController) }
            composable(HomeTab.Search.route) { Browse(navController) }
            composable(HomeTab.Profile.route) { Profile(navController) }
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
