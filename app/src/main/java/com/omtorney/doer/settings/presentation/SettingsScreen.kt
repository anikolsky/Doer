package com.omtorney.doer.settings.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.settings.presentation.components.ColorPickerDialog
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.ScreenName
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.settings.presentation.components.ColorType
import com.omtorney.doer.settings.presentation.components.DatabaseActions
import com.omtorney.doer.settings.presentation.components.MenuButton
import com.omtorney.doer.settings.presentation.components.MenuSlider
import com.omtorney.doer.settings.presentation.components.MenuSwitcher
import com.omtorney.doer.settings.presentation.components.SignActions
import com.omtorney.doer.settings.presentation.signin.SignInState
import com.omtorney.doer.settings.presentation.signin.UserData

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
    var colorPickerOpen by remember { mutableStateOf(false) }
    var colorType by remember { mutableStateOf<ColorType>(ColorType.Accent) }
    val scaffoldState = rememberScaffoldState()

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
                /** Separator size */
                MenuSlider(
                    color = accentColor,
                    title = { Text(text = "Separator size") },
                    subtitle = "Select separator size between notes",
                    value = noteSeparatorSize.toFloat(),
                    valueRange = (1f..15f),
                    onSlide = { viewModel.setNoteSeparatorSize(it.toInt()) }
                )
                Text(
                    text = noteSeparatorSize.toString(),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                /** Accent color */
                MenuButton(
                    color = accentColor,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_color),
                            contentDescription = null
                        )
                    },
                    title = {
                        Canvas(
                            modifier = Modifier
                                .size(width = 40.dp, height = 15.dp)
                                .clip(RoundedCornerShape(6))
                                .border(
                                    1.dp,
                                    MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                    RoundedCornerShape(6)
                                )
                                .background(color = Color(accentColor))
                        ) {}
                    },
                    subtitle = "Select accent color",
                    buttonText = "Select",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Accent
                    }
                )
                /** Secondary color */
                MenuButton(
                    color = accentColor,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_color),
                            contentDescription = null
                        )
                    },
                    title = {
                        Canvas(
                            modifier = Modifier
                                .size(width = 40.dp, height = 15.dp)
                                .clip(RoundedCornerShape(6))
                                .border(
                                    1.dp,
                                    MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                    RoundedCornerShape(6)
                                )
                                .background(color = Color(secondaryColor))
                        ) {}
                    },
                    subtitle = "Select secondary color",
                    buttonText = "Select",
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
                        if (ContextCompat.checkSelfPermission(
                                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.backupDatabase()
                            Toast.makeText(context, "Exported", Toast.LENGTH_SHORT).show()
                        } else {
                            navController.navigate(Screen.PermissionsRequest.route)
                        }
                    },
                    onImportClick = { viewModel.restoreDatabase() }
                )
                /** Account actions and info */
                SignActions(
                    color = accentColor,
                    title = { Text(text = "Account") },
                    subtitle = "Sign in with google account",
                    onSignInClick = onSignInClick,
                    onSignOutClick = onSignOutClick
                )
                Text(
                    text = "Name: ${userData?.username ?: "not logged in"}",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
        if (colorPickerOpen) {
            ColorPickerDialog(
                accentColor = accentColor,
                onChooseClick = { color ->
                    when (colorType) {
                        ColorType.Accent -> {
                            viewModel.setAccentColor(color)
                        }

                        ColorType.Secondary -> {
                            viewModel.setSecondaryColor(color)
                        }
                    }
                },
                onDismiss = { colorPickerOpen = false }
            )
        }
    }
}