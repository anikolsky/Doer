package com.omtorney.doer.core.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.omtorney.doer.notes.presentation.edit.NoteScreen
import com.omtorney.doer.notes.presentation.home.HomeScreen
import com.omtorney.doer.settings.presentation.SettingsScreen

@Composable
fun DoerApp() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colors.background)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Screen.Note.route + "?noteId=$noteId") {
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
        composable(
            route = Screen.Note.route + "?noteId={noteId}",
            arguments = listOf(navArgument(name = "noteId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            NoteScreen(onClickClose = { navController.popBackStack() })
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(onClickClose = { navController.popBackStack() })
        }
    }
}

private sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Note : Screen("note_screen")
    object Settings : Screen("settings_screen")
}