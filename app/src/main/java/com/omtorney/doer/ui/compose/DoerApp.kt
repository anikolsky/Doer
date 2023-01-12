package com.omtorney.doer.ui.compose

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omtorney.doer.presentation.HomeViewModel

@Composable
fun DoerApp() {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
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
                }
            )
        }
        composable(Screen.Note.route) {
            NoteScreen(
                viewModel = viewModel,
                onClickClose = { navController.popBackStack() }
            )
        }
    }
}

private sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Note : Screen("note")
}