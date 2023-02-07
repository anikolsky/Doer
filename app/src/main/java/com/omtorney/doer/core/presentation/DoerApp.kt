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
import com.omtorney.doer.R
import com.omtorney.doer.goals.presentation.goallist.GoalsScreen
import com.omtorney.doer.notes.presentation.noteedit.NoteScreen
import com.omtorney.doer.notes.presentation.notelist.HomeScreen
import com.omtorney.doer.settings.presentation.SettingsScreen

@Composable
fun DoerApp() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colors.background)

    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
    ) {
        composable(route = Screen.Notes.route) {
            HomeScreen(
                navController = navController,
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
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            NoteScreen(onClickClose = { navController.popBackStack() })
        }
        composable(route = Screen.Goals.route) {
            GoalsScreen(navController = navController,
                onAddGoalClick = {},
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(onClickClose = { navController.popBackStack() })
        }
    }
}

sealed class Screen(val route: String, val label: String?, val icon: Int?) {
    object Notes : Screen("notes_screen", "Notes", R.drawable.ic_round_task)
    object Note : Screen("note_screen", null, null)
    object Goals : Screen("goals_screen", "Goals", R.drawable.ic_round_flag_circle)
    object Settings : Screen("settings_screen", null, null)
}