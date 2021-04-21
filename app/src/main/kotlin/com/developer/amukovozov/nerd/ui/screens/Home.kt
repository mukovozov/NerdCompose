package ru.developer.amukovozov.nerd.ui.screens

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*

private const val KEY_ROUTE = "key_route"

@Composable
fun Home() {
    val navItems = HomeSections.values().toList()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                navItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = HomeSections.Feed.route) {
            composable(HomeSections.Feed.route) {}
            composable(HomeSections.Search.route) {}
            composable(HomeSections.Profile.route) {}
        }
    }
}

private enum class HomeSections(val route: String, val icon: ImageVector) {
    Feed("feed", Icons.Outlined.Home),
    Search("search", Icons.Outlined.Search),

    //    Notifications("notifications", Icons.Outlined.Notifications),
    Profile("profile", Icons.Outlined.AccountCircle)
}
