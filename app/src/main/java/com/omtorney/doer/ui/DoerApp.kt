package com.omtorney.doer.ui

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
import com.omtorney.doer.ui.home.HomeScreen
import com.omtorney.doer.ui.edit.NoteScreen
import com.omtorney.doer.ui.settings.SettingsScreen
import com.omtorney.doer.ui.home.HomeViewModel
import com.omtorney.doer.ui.settings.SettingsViewModel

@Composable
fun DoerApp() {
    val navController = rememberNavController()
//    val systemUiController = rememberSystemUiController()
//    val homeViewModel: HomeViewModel = hiltViewModel()
//    val accentColor by homeViewModel.accentColor.collectAsState()
//    systemUiController.setStatusBarColor(color = Color(accentColor))

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
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
            NoteScreen(onClickClose = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onClickClose = { navController.popBackStack() })
        }
    }
}

private sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Note : Screen("note")
    object Settings : Screen("settings")
}