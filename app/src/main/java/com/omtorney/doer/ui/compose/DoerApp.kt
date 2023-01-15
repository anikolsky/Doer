package com.omtorney.doer.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.omtorney.doer.ui.viewmodel.HomeViewModel
import com.omtorney.doer.ui.viewmodel.SettingsViewModel

@Composable
fun DoerApp() {
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val accentColor by homeViewModel.accentColor.collectAsState()

    systemUiController.setStatusBarColor(color = Color(accentColor))

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNoteClick = {
                    navController.navigate(Screen.Note.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }

                },
                onAddNoteClick = {
                    navController.navigate(Screen.Note.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.Note.route) {
            NoteScreen(
                viewModel = homeViewModel,
                onClickClose = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onClickClose = { navController.popBackStack() }
            )
        }
    }
}

private sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Note : Screen("note")
    object Settings : Screen("settings")
}