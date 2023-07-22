package com.omtorney.doer.presentation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.auth.api.identity.Identity
import com.omtorney.doer.firebase.auth.GoogleAuthUiClient
import com.omtorney.doer.presentation.components.RequestPermissions
import com.omtorney.doer.presentation.theme.DoerTheme
import com.omtorney.doer.presentation.screen.goals.goaledit.GoalEditScreen
import com.omtorney.doer.presentation.screen.goals.goaledit.GoalEditViewModel
import com.omtorney.doer.presentation.screen.goals.goallist.GoalsScreen
import com.omtorney.doer.presentation.screen.goals.goallist.GoalsViewModel
import com.omtorney.doer.presentation.screen.notes.noteedit.NoteEditScreen
import com.omtorney.doer.presentation.screen.notes.noteedit.NoteEditViewModel
import com.omtorney.doer.presentation.screen.notes.notelist.NotesScreen
import com.omtorney.doer.presentation.screen.notes.notelist.NotesViewModel
import com.omtorney.doer.presentation.screen.settings.SettingsScreen
import com.omtorney.doer.presentation.screen.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(Identity.getSignInClient(applicationContext))
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                DoerTheme {
                    val navController = rememberAnimatedNavController()
                    val mainViewModel = hiltViewModel<MainViewModel>()
                    val mainState by mainViewModel.mainState.collectAsStateWithLifecycle()
                    val signInState by mainViewModel.signInState.collectAsStateWithLifecycle()

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    mainViewModel.onSignInResult(signInResult)
                                }
                            }
                        }
                    )

                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.Notes.route,
                    ) {
                        composable(
                            route = Screen.Notes.route,
                        ) {
                            val viewModel = hiltViewModel<NotesViewModel>()
                            val notesState by viewModel.notesState

                            NotesScreen(
                                mainState = mainState,
                                notesState = notesState,
                                navController = navController,
                                onEvent = viewModel::onEvent,
                                onNoteClick = { noteId ->
                                    navController.navigate(Screen.NoteEdit.route + "?noteId=$noteId") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onAddNoteClick = {
                                    navController.navigate(Screen.NoteEdit.route) {
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
                            route = Screen.NoteEdit.route + "?noteId={noteId}",
                            arguments = listOf(navArgument(name = "noteId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }),
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(durationMillis = 400)
                                )
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(durationMillis = 400)
                                )
                            }
                        ) {
                            val viewModel = hiltViewModel<NoteEditViewModel>()
                            val noteEditState by viewModel.noteEditState

                            NoteEditScreen(
                                mainState = mainState,
                                noteEditState = noteEditState,
                                onEvent = viewModel::onEvent,
                                onClickClose = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.Goals.route
                        ) {
                            val viewModel = hiltViewModel<GoalsViewModel>()
                            val goalState by viewModel.goalState

                            GoalsScreen(
                                navController = navController,
                                mainState = mainState,
                                goalState = goalState,
                                onEvent = viewModel::onEvent,
                                onGoalClick = { goalId ->
                                    navController.navigate(Screen.GoalEdit.route + "?goalId=$goalId") {
                                        popUpTo(Screen.Goals.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onAddGoalClick = {
                                    navController.navigate(Screen.GoalEdit.route) {
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
                            route = Screen.GoalEdit.route + "?goalId={goalId}",
                            arguments = listOf(navArgument(name = "goalId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            })
                        ) {
                            val viewModel = hiltViewModel<GoalEditViewModel>()
                            val goalEditState by viewModel.goalEditState

                            GoalEditScreen(
                                mainState = mainState,
                                goalEditState = goalEditState,
                                onEvent = viewModel::onEvent,
                                onClickClose = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.Settings.route
                        ) {
                            val viewModel = hiltViewModel<SettingsViewModel>()
                            val firestoreUserState by viewModel.firestoreUserState.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                if (signInState.isSignInSuccessful) {
                                    Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_SHORT).show()
                                    mainViewModel.resetState()
                                }
                            }

                            SettingsScreen(
                                mainState = mainState,
                                signInState = signInState,
                                firestoreUserState = firestoreUserState,
                                firestoreUser = googleAuthUiClient.getSignedInUser(),
                                onEvent = viewModel::onEvent,
                                onClickClose = { navController.popBackStack() },
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                onSignOutClick = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(applicationContext,"Signed out", Toast.LENGTH_LONG).show()
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.PermissionsRequest.route
                        ) {
                            RequestPermissions(
                                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                onDismiss = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
