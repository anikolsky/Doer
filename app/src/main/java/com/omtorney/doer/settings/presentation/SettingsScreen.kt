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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.settings.presentation.components.ColorPickerDialog
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.ScreenName
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.settings.presentation.components.ColorType

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val context = LocalContext.current

    val accentColor by viewModel.accentColor.collectAsStateWithLifecycle()
    val secondaryColor by viewModel.secondaryColor.collectAsStateWithLifecycle()
    val lineDividerState by viewModel.lineSeparatorState.collectAsStateWithLifecycle()

    var colorPickerOpen by remember { mutableStateOf(false) }
    var colorType by remember { mutableStateOf<ColorType>(ColorType.Accent) }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                TopBar {
                    BackButton(onClick = onClickClose)
                    ScreenName(
                        title = Screen.Settings.label,
                        accentColor = accentColor
                    )
                    Spacer(modifier = Modifier)
                }
                /** Line divider switch */
                SettingsMenuSwitch(
                    title = { Text(text = "Enable divider") },
                    subtitle = "Add divider to the task list",
                    state = lineDividerState,
                    onCheckedChange = { viewModel.setLineSeparatorState(it) }
                )
                /** Accent color */
                SettingsMenuButton(
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
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Accent
                    }
                )
                /** Secondary color */
                SettingsMenuButton(
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
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Secondary
                    }
                )
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f) // TODO add an option
                )
                DatabaseActions(
                    color = accentColor,
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

@Composable
fun SettingsMenuSwitch(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    state: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        icon?.invoke()
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            title()
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
        Switch(
            checked = state,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(MaterialTheme.colors.primary)
        )
    }
}

@Composable
fun SettingsMenuButton(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        icon?.invoke()
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            title()
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
        ) {
            Text(text = "Select")
        }
    }
}

@Composable
fun DatabaseActions(
    color: Long,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Database",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )
        Button(
            onClick = onExportClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Export")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onImportClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Import")
        }
    }
}