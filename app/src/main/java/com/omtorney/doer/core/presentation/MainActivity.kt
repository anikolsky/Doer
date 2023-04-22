package com.omtorney.doer.core.presentation

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import com.omtorney.doer.core.presentation.components.RequestPermissions
import com.omtorney.doer.core.presentation.theme.DoerTheme
import com.omtorney.doer.goals.presentation.goaledit.GoalEditScreen
import com.omtorney.doer.goals.presentation.goallist.GoalsScreen
import com.omtorney.doer.notes.presentation.noteedit.NoteEditScreen
import com.omtorney.doer.notes.presentation.notelist.NotesScreen
import com.omtorney.doer.settings.presentation.SettingsScreen
import com.omtorney.doer.signin.GoogleAuthUiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                DoerTheme {
                    val mainViewModel: MainViewModel = hiltViewModel()
                    val navController = rememberAnimatedNavController()
                    val systemUiController = rememberSystemUiController()
                    val accentColor by mainViewModel.accentColor.collectAsStateWithLifecycle()
                    val secondaryColor by mainViewModel.secondaryColor.collectAsStateWithLifecycle()
                    val noteSeparatorState by mainViewModel.noteSeparatorState.collectAsStateWithLifecycle()
                    val noteSeparatorSize by mainViewModel.noteSeparatorSize.collectAsStateWithLifecycle()
                    systemUiController.setStatusBarColor(Color(accentColor))

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
                            // TODO val viewModel = viewModel<NotesViewModel>()
                            NotesScreen(
                                navController = navController,
                                accentColor = accentColor,
                                secondaryColor = secondaryColor,
                                noteSeparatorState = noteSeparatorState,
                                noteSeparatorSize = noteSeparatorSize,
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
                            NoteEditScreen(
                                accentColor = accentColor,
                                secondaryColor = secondaryColor,
                                onClickClose = { navController.popBackStack() }
                            )
                        }
                        composable(route = Screen.Goals.route) {
                            GoalsScreen(
                                navController = navController,
                                accentColor = accentColor,
                                secondaryColor = secondaryColor,
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
                            GoalEditScreen(
                                accentColor = accentColor,
                                secondaryColor = secondaryColor,
                                onClickClose = { navController.popBackStack() }
                            )
                        }
                        composable(route = Screen.Settings.route) {
                            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                if (signInState.isSignInSuccessful) {
                                    Toast.makeText(applicationContext, "Sign in successful", Toast.LENGTH_SHORT).show()
                                    mainViewModel.resetState()
                                }
                            }
                            SettingsScreen(
                                accentColor = accentColor,
                                secondaryColor = secondaryColor,
                                noteSeparatorState = noteSeparatorState,
                                noteSeparatorSize = noteSeparatorSize,
                                signInState = signInState,
                                userData = googleAuthUiClient.getSignedInUser(),
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
                        composable(route = Screen.PermissionsRequest.route) {
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
