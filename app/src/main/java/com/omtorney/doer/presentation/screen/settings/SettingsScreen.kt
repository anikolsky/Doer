package com.omtorney.doer.presentation.screen.settings

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.firebase.auth.SignInState
import com.omtorney.doer.firebase.presentation.FirestoreListItem
import com.omtorney.doer.firebase.presentation.FirestoreUserState
import com.omtorney.doer.presentation.MainState
import com.omtorney.doer.presentation.Screen
import com.omtorney.doer.presentation.components.BackButton
import com.omtorney.doer.presentation.components.ScreenName
import com.omtorney.doer.presentation.components.TopBar
import com.omtorney.doer.presentation.screen.settings.components.ColorPickerDialog
import com.omtorney.doer.presentation.screen.settings.components.ColorType
import com.omtorney.doer.presentation.screen.settings.components.MenuColor
import com.omtorney.doer.presentation.screen.settings.components.MenuSlider
import com.omtorney.doer.presentation.screen.settings.components.MenuSwitcher
import com.omtorney.doer.presentation.screen.settings.components.SignActions

@Composable
fun SettingsScreen(
    mainState: MainState,
    signInState: SignInState,
    firestoreUserState: FirestoreUserState,
    firestoreUser: FirestoreUser?,
    onEvent: (SettingsEvent) -> Unit,
    onClickClose: () -> Unit,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    val context = LocalContext.current
    var colorPickerOpen by remember { mutableStateOf(false) }
    var colorType by remember { mutableStateOf<ColorType>(ColorType.Accent) }

    LaunchedEffect(key1 = signInState.signInErrorMessage) {
        signInState.signInErrorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopBar {
                BackButton(onClick = onClickClose)
                ScreenName(
                    title = Screen.Settings.label,
                    accentColor = mainState.accentColor
                )
                Spacer(modifier = Modifier)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                /** NoteEdit separator switcher */
                MenuSwitcher(
                    accentColor = Color(mainState.accentColor),
                    title = { Text(text = "Enable separation") },
                    subtitle = "Add separation to the task list",
                    state = mainState.noteSeparatorState,
                    onCheckedChange = { onEvent(SettingsEvent.SetNoteSeparatorState(it)) }
                )
                /** NoteEdit separator size */
                MenuSlider(
                    color = mainState.accentColor,
                    title = { Text(text = "Separator size") },
                    subtitle = "Set spacing between notes",
                    value = mainState.noteSeparatorSize.toFloat(),
                    valueRange = (1f..15f),
                    onSlide = { onEvent(SettingsEvent.SetNoteSeparatorSize(it.toInt())) }
                )
                /** Accent color picker */
                MenuColor(
                    accentColor = mainState.accentColor,
                    backgroundColor = mainState.accentColor,
                    subtitle = "Select accent color",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Accent
                    }
                )
                /** Secondary color picker */
                MenuColor(
                    accentColor = mainState.accentColor,
                    backgroundColor = mainState.secondaryColor,
                    subtitle = "Select secondary color",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Secondary
                    }
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                /** Sign in/out */
                SignActions(
                    color = mainState.accentColor,
                    title = { Text(text = "Account actions") },
                    subtitle = "Using google account",
                    onSignInClick = onSignInClick,
                    onSignOutClick = onSignOutClick
                )
                /** Firestore */
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .border(
                            width = 1.dp,
                            color = if (firestoreUser != null)
                                Color(mainState.secondaryColor).copy(alpha = 0.5f)
                            else
                                Color(mainState.accentColor),
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    /** Account info */
                    Text(
                        text = "Account name: ${firestoreUser?.name ?: "not logged in"}",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                    /** Backup button */
                    Button(
                        enabled = firestoreUser?.name != null,
                        onClick = {
                            if (firestoreUser != null) {
                                onEvent(
                                    SettingsEvent.CreateUser(
                                        user = firestoreUser,
                                        context = context
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(mainState.secondaryColor).copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 0.dp)
                    ) {
                        Text("Backup ${firestoreUser?.name ?: "unavailable"}")
                    }
                    when {
                        firestoreUserState.isLoading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                        !firestoreUserState.errorMessage.isNullOrEmpty() -> {
                            Text(
                                text = firestoreUserState.errorMessage,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        firestoreUserState.data.isNullOrEmpty() -> {
                            Text(
                                text = "Empty",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        firestoreUserState.data.isNotEmpty() -> {
                            LazyColumn(
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 0.dp
                                )
                            ) {
                                items(items = firestoreUserState.data) { item ->
                                    FirestoreListItem(
                                        userName = item?.name!!,
                                        userId = item.id!!,
                                        onItemClick = {}, // TODO remove
                                        onDeleteClick = { id, name ->
                                            onEvent(
                                                SettingsEvent.DeleteUser(
                                                    FirestoreUser(id, name),
                                                    context
                                                )
                                            )
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
                accentColor = mainState.accentColor,
                onChooseClick = { color ->
                    when (colorType) {
                        ColorType.Accent -> {
                            onEvent(SettingsEvent.SetAccentColor(color))
                        }

                        ColorType.Secondary -> {
                            onEvent(SettingsEvent.SetSecondaryColor(color))
                        }
                    }
                },
                onDismiss = { colorPickerOpen = false }
            )
        }
    }
}
