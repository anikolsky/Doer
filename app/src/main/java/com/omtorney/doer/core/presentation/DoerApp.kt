package com.omtorney.doer.core.presentation

import android.Manifest
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.omtorney.doer.core.presentation.components.RequestPermissions
import com.omtorney.doer.goals.presentation.goaledit.GoalEditScreen
import com.omtorney.doer.goals.presentation.goallist.GoalsScreen
import com.omtorney.doer.notes.presentation.noteedit.NoteEditScreen
import com.omtorney.doer.notes.presentation.notelist.NotesScreen
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
            NotesScreen(
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
            NoteEditScreen(onClickClose = { navController.popBackStack() })
        }
        composable(route = Screen.Goals.route) {
            GoalsScreen(
                navController = navController,
                onGoalClick = { goalId ->
                    navController.navigate(Screen.Goal.route + "?goalId=$goalId") {
                        popUpTo(Screen.Goals.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onAddGoalClick = {
                    navController.navigate(Screen.Goal.route) {
                        popUpTo(Screen.Goals.route) { saveState = true }
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
            route = Screen.Goal.route + "?goalId={goalId}",
            arguments = listOf(navArgument(name = "goalId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) {
            GoalEditScreen(onClickClose = { navController.popBackStack() })
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onClickClose = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(route = Screen.PermissionsRequest.route) {
            RequestPermissions(
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}