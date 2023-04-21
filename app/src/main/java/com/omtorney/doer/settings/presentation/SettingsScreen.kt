package com.omtorney.doer.settings.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.ScreenName
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.firestore.data.FirestoreUser
import com.omtorney.doer.firestore.presentation.FirestoreListItem
import com.omtorney.doer.settings.presentation.components.ColorPickerDialog
import com.omtorney.doer.settings.presentation.components.ColorType
import com.omtorney.doer.settings.presentation.components.DatabaseActions
import com.omtorney.doer.settings.presentation.components.MenuColor
import com.omtorney.doer.settings.presentation.components.MenuSlider
import com.omtorney.doer.settings.presentation.components.MenuSwitcher
import com.omtorney.doer.settings.presentation.components.SignActions
import com.omtorney.doer.signin.SignInState
import com.omtorney.doer.signin.UserData

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    accentColor: Long,
    secondaryColor: Long,
    noteSeparatorState: Boolean,
    noteSeparatorSize: Int,
    signInState: SignInState,
    userData: UserData?,
    viewModel: SettingsViewModel = hiltViewModel(),
    onClickClose: () -> Unit,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    var colorPickerOpen by remember { mutableStateOf(false) }
    var colorType by remember { mutableStateOf<ColorType>(ColorType.Accent) }

    val firestoreUserState by viewModel.firestoreUserState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = signInState.signInErrorMessage) {
        signInState.signInErrorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopBar(color = accentColor) {
                BackButton(onClick = onClickClose)
                ScreenName(
                    title = Screen.Settings.label,
                    accentColor = accentColor
                )
                Spacer(modifier = Modifier)
            }
        },
        modifier = modifier,
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                /** Note separator switcher */
                MenuSwitcher(
                    color = accentColor,
                    title = { Text(text = "Enable separation") },
                    subtitle = "Add separation to the task list",
                    state = noteSeparatorState,
                    onCheckedChange = { viewModel.setNoteSeparatorState(it) }
                )
                /** Note separator size */
                MenuSlider(
                    color = accentColor,
                    title = { Text(text = "Separator size") },
                    subtitle = "Select separator size between notes",
                    value = noteSeparatorSize.toFloat(),
                    valueRange = (1f..15f),
                    onSlide = { viewModel.setNoteSeparatorSize(it.toInt()) }
                )
                Text(
                    text = "$noteSeparatorSize",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                /** Accent color picker */
                MenuColor(
                    accentColor = accentColor,
                    backgroundColor = accentColor,
                    subtitle = "Select accent color",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Accent
                    }
                )
                /** Secondary color picker */
                MenuColor(
                    accentColor = accentColor,
                    backgroundColor = secondaryColor,
                    subtitle = "Select secondary color",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Secondary
                    }
                )
                /** Export/import database buttons */
                DatabaseActions(
                    color = accentColor,
                    title = { Text(text = "Database") },
                    subtitle = "Export and import database",
                    onExportClick = {
//                        if (ContextCompat.checkSelfPermission(
//                                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            ) == PackageManager.PERMISSION_GRANTED
//                        ) {
//                            viewModel.backupDatabase()
//                            Toast.makeText(context, "Exported", Toast.LENGTH_SHORT).show()
//                        } else {
//                            navController.navigate(Screen.PermissionsRequest.route)
//                        }
                    },
                    onImportClick = { viewModel.restoreDatabase() }
                )
                /** Sign in/out */
                SignActions(
                    color = accentColor,
                    title = { Text(text = "Account actions") },
                    subtitle = "Using google account",
                    onSignInClick = onSignInClick,
                    onSignOutClick = onSignOutClick
                )
                /** Account info */
                Text(
                    text = "Account name: ${userData?.username ?: "not logged in"}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                /** Firestore */
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Button(
                        enabled = userData?.username != null,
                        onClick = {
                            if (userData != null) {
                                viewModel.createUser(
                                    firestoreUserName = userData.username!!,
                                    context = context
                                )
                            }
                    }) {
                        Text("Backup ${userData?.username ?: "unavailable"}")
                    }
                    when {
                        firestoreUserState.isLoading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        !firestoreUserState.errorMessage.isNullOrEmpty() -> {
                            Text(text = firestoreUserState.errorMessage!!)
                        }
                        firestoreUserState.data.isNullOrEmpty() -> {
                            Text(text = "Empty")
                        }
                        !firestoreUserState.data.isNullOrEmpty() -> {
                            LazyColumn {
                                items(firestoreUserState.data ?: emptyList()) { item ->
                                    FirestoreListItem(
                                        userName = item?.name!!,
                                        userId = item.id!!,
                                        onUpdateClick = { id, name ->
                                            viewModel.updateUser(FirestoreUser(id, name), context)
                                        },
                                        onDeleteClick = { id, name ->
                                            viewModel.deleteUser(FirestoreUser(id, name), context)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (colorPickerOpen) {
            ColorPickerDialog(
                accentColor = accentColor,
                onChooseClick = { color ->
                    when (colorType) {
                        ColorType.Accent -> { viewModel.setAccentColor(color) }
                        ColorType.Secondary -> { viewModel.setSecondaryColor(color) }
                    }
                },
                onDismiss = { colorPickerOpen = false }
            )
        }
    }
}